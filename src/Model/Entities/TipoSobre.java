package Model.Entities;


public enum TipoSobre {
    BRONCE(70, 20, 9, 1),
    PLATA(40, 40, 15, 5),
    ORO(10, 30, 45, 15);

    private final int probComun;
    private final int probRaro;
    private final int probEpico;
    private final int probLegendario;

    TipoSobre(int probComun, int probRaro, int probEpico, int probLegendario) {
        this.probComun = probComun;
        this.probRaro = probRaro;
        this.probEpico = probEpico;
        this.probLegendario = probLegendario;
    }

    public int getProbComun() { return probComun; }
    public int getProbRaro() { return probRaro; }
    public int getProbEpico() { return probEpico; }
    public int getProbLegendario() { return probLegendario; }
}
