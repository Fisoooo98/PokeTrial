package Model.Entities;


public enum TipoSobre {
    BRONCE(70, 20, 9, 1,1),
    PLATA(40, 40, 15, 5,5),
    ORO(10, 30, 45, 40,10);

    private final int probComun;
    private final int probRaro;
    private final int probEpico;
    private final int probLegendario;
    private final int probUltiEvo;

    TipoSobre(int probComun, int probRaro, int probEpico, int probLegendario,int probUltiEvo) {
        this.probComun = probComun;
        this.probRaro = probRaro;
        this.probEpico = probEpico;
        this.probLegendario = probLegendario;
        this.probUltiEvo = probUltiEvo;
    }

    public int getProbComun() { return probComun; }
    public int getProbRaro() { return probRaro; }
    public int getProbEpico() { return probEpico; }
    public int getProbLegendario() { return probLegendario; }

    public int getProbUltiEvo() {
        return probUltiEvo;
    }
}