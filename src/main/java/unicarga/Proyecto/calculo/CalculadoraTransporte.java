package unicarga.Proyecto.calculo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Cálculo de cubicaje por suma de volúmenes.
 *
 * <p>Dado el contenido de un pedido y el catálogo de unidades de transporte, busca las
 * combinaciones de unidades (p. ej. "1 Camión + 1 Van") que cumplan TODO lo siguiente:</p>
 * <ol>
 *   <li>Volumen: la suma de (volumen físico × factor de aprovechamiento) de las unidades
 *       es mayor o igual que la suma de volúmenes de los productos.</li>
 *   <li>Peso: la suma de los pesos máximos de las unidades es mayor o igual que el peso total.</li>
 *   <li>Encaje: cada producto cabe (por dimensiones y peso individual) en al menos una de
 *       las unidades de la combinación.</li>
 *   <li>Mínima: no sobra ninguna unidad (si se quita una, deja de cumplirse algo).</li>
 * </ol>
 *
 * <p>Ranking: menos unidades primero; a igualdad, menos espacio libre (menos desperdicio);
 * a igualdad, menos tipos distintos mezclados.</p>
 *
 * <p>Esta clase no depende de Spring ni de JPA. Unidades: cm, cm³ y kg de entrada;
 * m³ y kg de salida.</p>
 */
public class CalculadoraTransporte {

    private static final BigDecimal CM3_POR_M3 = new BigDecimal("1000000");
    private static final BigDecimal CIEN = BigDecimal.valueOf(100);
    private static final int ESCALA_M3 = 4;

    private final BigDecimal factor;
    private final int maxUnidades;
    private final int maxCombinaciones;

    /**
     * @param factorAprovechamiento fracción utilizable del volumen de cada unidad, en (0, 1]
     * @param maxUnidades           máximo de unidades que puede tener una combinación
     * @param maxCombinaciones      cuántas combinaciones se devuelven como máximo
     */
    public CalculadoraTransporte(BigDecimal factorAprovechamiento, int maxUnidades, int maxCombinaciones) {
        if (factorAprovechamiento == null
                || factorAprovechamiento.signum() <= 0
                || factorAprovechamiento.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("El factor de aprovechamiento debe ser mayor que 0 y menor o igual que 1.");
        }
        if (maxUnidades < 1 || maxCombinaciones < 1) {
            throw new IllegalArgumentException("maxUnidades y maxCombinaciones deben ser al menos 1.");
        }
        this.factor = factorAprovechamiento;
        this.maxUnidades = maxUnidades;
        this.maxCombinaciones = maxCombinaciones;
    }

    // ------------------------------------------------------------------ API

    public ResultadoCalculo calcular(List<ItemCarga> itemsEntrada, List<TipoUnidad> tiposEntrada) {
        List<ItemCarga> items = itemsEntrada.stream().filter(i -> i.cantidad() > 0).toList();

        BigDecimal volumenCm3 = items.stream().map(ItemCarga::volumenTotalCm3)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal pesoKg = items.stream().map(ItemCarga::pesoTotalKg)
                .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
        BigDecimal volumenM3 = aM3(volumenCm3);

        if (items.isEmpty()) {
            return ResultadoCalculo.noViable("El pedido no tiene productos.", volumenM3, pesoKg, factor);
        }

        // Tipos utilizables, en orden estable (por id) para que el resultado sea determinista.
        List<TipoUnidad> tipos = tiposEntrada.stream()
                .filter(t -> t.volumenCm3().signum() > 0 && t.pesoMaximo().signum() > 0)
                .sorted(Comparator.comparing(TipoUnidad::id, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
        if (tipos.isEmpty()) {
            return ResultadoCalculo.noViable(
                    "No hay unidades de transporte válidas en el catálogo (medidas y peso mayores a cero).",
                    volumenM3, pesoKg, factor);
        }

        // ¿Qué productos caben en qué tipo de unidad?
        int n = tipos.size();
        BitSet[] cubre = new BitSet[n];
        for (int t = 0; t < n; t++) {
            cubre[t] = new BitSet(items.size());
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).cabeEn(tipos.get(t))) {
                    cubre[t].set(i);
                }
            }
        }
        BitSet cubiertos = new BitSet(items.size());
        for (BitSet b : cubre) {
            cubiertos.or(b);
        }
        if (cubiertos.cardinality() < items.size()) {
            String nombres = java.util.stream.IntStream.range(0, items.size())
                    .filter(i -> !cubiertos.get(i))
                    .mapToObj(i -> items.get(i).nombre())
                    .collect(Collectors.joining(", "));
            return ResultadoCalculo.noViable(
                    "Ninguna unidad del catálogo puede llevar: " + nombres
                            + " (no cabe por sus dimensiones o excede el peso máximo de la unidad).",
                    volumenM3, pesoKg, factor);
        }

        // Búsqueda de combinaciones.
        Contexto ctx = new Contexto(items.size(), tipos, factor, volumenCm3, pesoKg, cubre);
        buscar(0, maxUnidades, new int[n], BigDecimal.ZERO, BigDecimal.ZERO, ctx);

        if (ctx.candidatas.isEmpty()) {
            return ResultadoCalculo.noViable(
                    "No se encontró ninguna combinación de hasta " + maxUnidades + " unidades que soporte "
                            + volumenM3.toPlainString() + " m³ y " + pesoKg.toPlainString() + " kg. "
                            + "Registra unidades de mayor capacidad en el catálogo o divide el pedido.",
                    volumenM3, pesoKg, factor);
        }

        // Ranking.
        List<int[]> ordenadas = new ArrayList<>(ctx.candidatas);
        ordenadas.sort((a, b) -> {
            int c = Integer.compare(total(a), total(b));
            if (c != 0) return c;
            c = volumenUtil(a, ctx).compareTo(volumenUtil(b, ctx));
            if (c != 0) return c;
            c = Integer.compare(distintos(a), distintos(b));
            if (c != 0) return c;
            return Arrays.compare(a, b);
        });

        List<Combinacion> resultado = new ArrayList<>();
        int limite = Math.min(maxCombinaciones, ordenadas.size());
        for (int p = 0; p < limite; p++) {
            resultado.add(construir(p + 1, ordenadas.get(p), ctx, volumenM3, pesoKg));
        }
        return new ResultadoCalculo(true, null, volumenM3, pesoKg, factor, List.copyOf(resultado));
    }

    // ------------------------------------------------------------------ búsqueda

    /**
     * Recorre las cantidades de cada tipo (0..restantes). En cuanto una asignación cumple todo,
     * se registra y se deja de agregar unidades de ese tipo, porque cualquier combinación con
     * más unidades dejaría de ser mínima.
     */
    private void buscar(int idx, int restantes, int[] cuentas, BigDecimal vol, BigDecimal peso, Contexto c) {
        if (idx == c.n || restantes == 0) {
            return;
        }
        // Poda: aunque todas las unidades restantes fueran del tipo más grande, no alcanzaría.
        BigDecimal rest = BigDecimal.valueOf(restantes);
        if (vol.add(c.maxVolDesde[idx].multiply(rest)).compareTo(c.volRequerido) < 0) return;
        if (peso.add(c.maxPesoDesde[idx].multiply(rest)).compareTo(c.pesoRequerido) < 0) return;

        cuentas[idx] = 0;
        buscar(idx + 1, restantes, cuentas, vol, peso, c);

        for (int k = 1; k <= restantes; k++) {
            cuentas[idx] = k;
            BigDecimal kk = BigDecimal.valueOf(k);
            BigDecimal v = vol.add(c.volUtil[idx].multiply(kk));
            BigDecimal p = peso.add(c.pesoMax[idx].multiply(kk));
            if (esFactible(cuentas, v, p, c)) {
                if (esMinima(cuentas, c)) {
                    c.candidatas.add(cuentas.clone());
                }
                break;
            }
            buscar(idx + 1, restantes - k, cuentas, v, p, c);
        }
        cuentas[idx] = 0;
    }

    private boolean esFactible(int[] cuentas, BigDecimal vol, BigDecimal peso, Contexto c) {
        if (vol.compareTo(c.volRequerido) < 0 || peso.compareTo(c.pesoRequerido) < 0) {
            return false;
        }
        BitSet union = new BitSet(c.nItems);
        for (int i = 0; i < c.n; i++) {
            if (cuentas[i] > 0) {
                union.or(c.cubre[i]);
            }
        }
        return union.cardinality() == c.nItems;
    }

    /** Mínima: quitar cualquier unidad rompe volumen, peso o encaje. */
    private boolean esMinima(int[] cuentas, Contexto c) {
        for (int i = 0; i < c.n; i++) {
            if (cuentas[i] == 0) continue;
            cuentas[i]--;
            boolean sigueValida = esFactible(cuentas, volumenUtil(cuentas, c), pesoMaximo(cuentas, c), c);
            cuentas[i]++;
            if (sigueValida) {
                return false;
            }
        }
        return true;
    }

    // ------------------------------------------------------------------ construcción del resultado

    private Combinacion construir(int posicion, int[] cuentas, Contexto c,
                                  BigDecimal usadoM3, BigDecimal pesoTotalKg) {
        List<LineaCombinacion> lineas = new ArrayList<>();
        BigDecimal fisicoCm3 = BigDecimal.ZERO;
        for (int i = 0; i < c.n; i++) {
            if (cuentas[i] > 0) {
                TipoUnidad t = c.tipos.get(i);
                lineas.add(new LineaCombinacion(t.id(), t.nombre(), cuentas[i]));
                fisicoCm3 = fisicoCm3.add(t.volumenCm3().multiply(BigDecimal.valueOf(cuentas[i])));
            }
        }
        BigDecimal utilM3 = aM3(volumenUtil(cuentas, c));
        BigDecimal libreM3 = utilM3.subtract(usadoM3);
        BigDecimal pctUsado = utilM3.signum() == 0 ? BigDecimal.ZERO.setScale(1)
                : usadoM3.multiply(CIEN).divide(utilM3, 1, RoundingMode.HALF_UP);

        BigDecimal capPeso = pesoMaximo(cuentas, c).setScale(2, RoundingMode.HALF_UP);
        BigDecimal pctPeso = capPeso.signum() == 0 ? BigDecimal.ZERO.setScale(1)
                : pesoTotalKg.multiply(CIEN).divide(capPeso, 1, RoundingMode.HALF_UP);

        return new Combinacion(posicion, posicion == 1, List.copyOf(lineas), total(cuentas),
                aM3(fisicoCm3), utilM3, usadoM3, libreM3, pctUsado, capPeso, pctPeso);
    }

    // ------------------------------------------------------------------ utilidades

    private static BigDecimal aM3(BigDecimal cm3) {
        return cm3.divide(CM3_POR_M3, ESCALA_M3, RoundingMode.HALF_UP);
    }

    private static int total(int[] cuentas) {
        int s = 0;
        for (int x : cuentas) s += x;
        return s;
    }

    private static int distintos(int[] cuentas) {
        int s = 0;
        for (int x : cuentas) if (x > 0) s++;
        return s;
    }

    private static BigDecimal volumenUtil(int[] cuentas, Contexto c) {
        BigDecimal s = BigDecimal.ZERO;
        for (int i = 0; i < c.n; i++) {
            if (cuentas[i] > 0) s = s.add(c.volUtil[i].multiply(BigDecimal.valueOf(cuentas[i])));
        }
        return s;
    }

    private static BigDecimal pesoMaximo(int[] cuentas, Contexto c) {
        BigDecimal s = BigDecimal.ZERO;
        for (int i = 0; i < c.n; i++) {
            if (cuentas[i] > 0) s = s.add(c.pesoMax[i].multiply(BigDecimal.valueOf(cuentas[i])));
        }
        return s;
    }

    /** Datos precalculados que comparte la búsqueda. */
    private static final class Contexto {
        final int n;
        final int nItems;
        final List<TipoUnidad> tipos;
        final BigDecimal[] volUtil;
        final BigDecimal[] pesoMax;
        final BigDecimal[] maxVolDesde;
        final BigDecimal[] maxPesoDesde;
        final BigDecimal volRequerido;
        final BigDecimal pesoRequerido;
        final BitSet[] cubre;
        final List<int[]> candidatas = new ArrayList<>();

        Contexto(int nItems, List<TipoUnidad> tipos, BigDecimal factor,
                 BigDecimal volRequerido, BigDecimal pesoRequerido, BitSet[] cubre) {
            this.n = tipos.size();
            this.nItems = nItems;
            this.tipos = tipos;
            this.volRequerido = volRequerido;
            this.pesoRequerido = pesoRequerido;
            this.cubre = cubre;
            this.volUtil = new BigDecimal[n];
            this.pesoMax = new BigDecimal[n];
            for (int i = 0; i < n; i++) {
                volUtil[i] = tipos.get(i).volumenCm3().multiply(factor);
                pesoMax[i] = tipos.get(i).pesoMaximo();
            }
            this.maxVolDesde = new BigDecimal[n];
            this.maxPesoDesde = new BigDecimal[n];
            BigDecimal mv = BigDecimal.ZERO;
            BigDecimal mp = BigDecimal.ZERO;
            for (int i = n - 1; i >= 0; i--) {
                mv = mv.max(volUtil[i]);
                mp = mp.max(pesoMax[i]);
                maxVolDesde[i] = mv;
                maxPesoDesde[i] = mp;
            }
        }
    }
}
