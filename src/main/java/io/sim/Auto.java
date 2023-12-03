package io.sim;

import de.tudresden.sumo.cmd.Vehicle;
import java.util.ArrayList;

import it.polito.appeal.traci.SumoTraciConnection;
import de.tudresden.sumo.objects.SumoColor;
import de.tudresden.sumo.objects.SumoPosition2D;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class Auto extends Thread {

    private String idAuto;
    private SumoColor colorAuto;
    private String driverID;
    private SumoTraciConnection sumo;

    private boolean on_off;
    private long acquisitionRate;
    private int fuelType;               // 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
    private int fuelPreferential;       // 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
    private double fuelPrice; 		// price in liters
    private int personCapacity;		// the total number of persons that can ride in this vehicle
    private int personNumber;		// the total number of persons which are riding in this vehicle
    Driver driver;
    private ArrayList<DrivingData> drivingRepport;
    private double odometer;
    private double consumption;
    private static double aX, bX, aY, bY;
    public Auto(boolean _on_off, String _idAuto, SumoColor _colorAuto, String _driverID, SumoTraciConnection _sumo, long _acquisitionRate,
            int _fuelType, int _fuelPreferential, double _fuelPrice, int _personCapacity, int _personNumber, Driver driver) throws Exception {

        this.on_off = _on_off;
        this.idAuto = _idAuto;
        this.colorAuto = _colorAuto;
        this.driverID = _driverID;
        this.sumo = _sumo;
        this.acquisitionRate = _acquisitionRate;
        this.driver = driver;
        this.odometer = 0;
        this.setConsumption(0);

        if ((_fuelType < 0) || (_fuelType > 4)) {
            this.fuelType = 4;
        } else {
            this.fuelType = _fuelType;
        }

        if ((_fuelPreferential < 0) || (_fuelPreferential > 4)) {
            this.fuelPreferential = 4;
        } else {
            this.fuelPreferential = _fuelPreferential;
        }

        this.fuelPrice = _fuelPrice;
        this.personCapacity = _personCapacity;
        this.personNumber = _personNumber;
        this.drivingRepport = new ArrayList<DrivingData>();
        
        // Pontos conhecidos
        double x1 = 3442.99;
        double y1 = -22.967229;
        double x2 = 2117.67;
        double y2 = -43.18373;

        // Novo ponto a ser convertido
        double newX1 = -22.967229;
        double newY1 = -43.18373;
        
        // Criar uma instância do CoordenadaConverter
        CoordenadaConverter(x1, y1, x2, y2, newX1, newY1);

    }
    public static String format(double x) {
        return String.format("%.2f", x);
    }
    @Override
    public void run() {
        DecimalFormat df = new DecimalFormat("#,###.00");

        double distanciaPercorridaTotal = 0;
        long tempoInicial = 0;
        appendToCSV2("relatorio2.csv","X;Y;Tempo;odometro;Distancia;Distancia Total;Velocidade;Consumo;Gasto");
        while (this.on_off) {
            try {
                if (this.getOdometer() - driver.getCar().getLastRead() > 1) {
                    double consumo = this.getConsumption() / 1000;
                    double consumoL = this.getConsumption() / (0.74 * 1000000);
                    if (consumo < 50) {
                        String log = "";
                        double auxspeed = ((double) sumo.do_job_get(Vehicle.getSpeed(this.idAuto)));
                        long tempo = this.drivingRepport.get(this.drivingRepport.size() - 1).getTimeStamp();
                        if(tempoInicial == 0){
                            tempoInicial = tempo;
                        }else{
                            tempo = tempo - tempoInicial;
                        }
                        double distanciaPercorrida = this.getOdometer() - driver.getCar().getLastRead(); // Distância percorrida em metros                                                
                        distanciaPercorridaTotal += distanciaPercorrida;
                        //double combustivelGasto = distanciaPercorrida / consumo; // Combustível gasto em litros
                        int km = (int) Math.round(distanciaPercorrida);
                        log+="***********************\n";
                        log+="Carro: "+driver.getCar().getCarId()+"\n";
                        //log+=("Combustível gasto: " + combustivelGasto + " litros "+distanciaPercorrida+" "+km+"\n");                        
                        driver.getCar().setLastRead(driver.getCar().getLastRead()+km);
                        log+="Tanque atual: "+(driver.getCar().getFuelTank()+"\n");
                       // driver.getCar().setFuelTank(driver.getCar().getFuelTank()-combustivelGasto);
                        driver.receberPagamento(km);
                        System.out.println(driver.getCar().getFuelTank());
                        if(driver.getCar().getFuelTank() <= 3){
                            log+=("Menos de 3 => "+driver.getCar().getFuelTank()+" abastecendo...\n");
                            driver.abastecer();
                            log+="Tanque após abastecer: "+(driver.getCar().getFuelTank()+"\n");
                        }
                        log+="Conta Motorista: "+(((double)driver.getAccount().getBalance()/100)+"\n***********************\n");
                        double consumomgs = (consumo*100)/1000;
                        appendToCSV2("relatorio2.csv", format(this.drivingRepport.get(this.drivingRepport.size() - 1).getX_Position())+";"+format(this.drivingRepport.get(this.drivingRepport.size() - 1).getY_Position())+";"+format(tempo/1000)+";"+format(this.getOdometer())+";"+format(distanciaPercorrida)+";"+format(distanciaPercorridaTotal)+";"+format(auxspeed*3.9)+";"+format(consumoL)+";"+format((3.25*consumoL)*(tempo/1000)));
                        System.out.println(this.drivingRepport.get(this.drivingRepport.size() - 1).getX_Position()+" "+this.drivingRepport.get(this.drivingRepport.size() - 1).getY_Position()+" - Tempo = "+(tempo/1000)+", Odometro = "+this.getOdometer()+", Distancia = "+distanciaPercorrida+", Distancia total = "+distanciaPercorridaTotal+", Velocidade = "+(auxspeed*3.9)+", Consumo = "+(consumoL)+", Gasto = "+((3.25*consumoL)*(tempo/1000)));
                    }
                } else {
//                    System.out.println(this.getOdometer()+" -- "+driver.getCar().getLastRead());
                }
                //Auto.sleep(this.acquisitionRate);
                //System.out.println("jjj");
                this.atualizaSensores();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void CoordenadaConverter(double x1, double y1, double x2, double y2, double newX1, double newY1) {
        this.aX = (y2 - y1) / (x2 - x1);
        this.bX = y1 - aX * x1;
        this.aY = (newY1 - y1) / (x2 - x1);
        this.bY = y1 - aY * x1;
    }
    public static double[] converterCoordenadas(double x, double y) {
        double newX = aX * x + bX;
        double newY = aY * y + bY;
        return new double[]{newX, newY};
    }
    private void atualizaSensores() {

        try {

            if (!this.getSumo().isClosed()) {
                SumoPosition2D sumoPosition2D;
                if(Vehicle.getPosition(this.idAuto)!=null){
                    if(sumo.do_job_get(Vehicle.getPosition(this.idAuto)) != null){
                        sumoPosition2D = (SumoPosition2D) sumo.do_job_get(Vehicle.getPosition(this.idAuto));

//                System.out.println(this.getIdAuto()+" AutoID: " + this.getIdAuto());
//                System.out.println(this.getIdAuto()+" RoadID: " + (String) this.sumo.do_job_get(Vehicle.getRoadID(this.idAuto)));
//                System.out.println(this.getIdAuto()+" RouteID: " + (String) this.sumo.do_job_get(Vehicle.getRouteID(this.idAuto)));
//                System.out.println(this.getIdAuto()+" RouteIndex: " + this.sumo.do_job_get(Vehicle.getRouteIndex(this.idAuto)));
                DrivingData _repport = new DrivingData(
                        this.idAuto, this.driverID, System.currentTimeMillis(), sumoPosition2D.x, sumoPosition2D.y,
                        (String) this.sumo.do_job_get(Vehicle.getRoadID(this.idAuto)),
                        (String) this.sumo.do_job_get(Vehicle.getRouteID(this.idAuto)),
                        (double) sumo.do_job_get(Vehicle.getSpeed(this.idAuto)),
                        (double) sumo.do_job_get(Vehicle.getDistance(this.idAuto)),
                        (double) sumo.do_job_get(Vehicle.getFuelConsumption(this.idAuto)),
                        // Vehicle's fuel consumption in ml/s during this time step,
                        // to get the value for one step multiply with the step length; error value:
                        // -2^30

                        1/*averageFuelConsumption (calcular)*/,
                        this.fuelType, this.fuelPrice,
                        (double) sumo.do_job_get(Vehicle.getCO2Emission(this.idAuto)),
                        // Vehicle's CO2 emissions in mg/s during this time step,
                        // to get the value for one step multiply with the step length; error value:
                        // -2^30

                        (double) sumo.do_job_get(Vehicle.getHCEmission(this.idAuto)),
                        // Vehicle's HC emissions in mg/s during this time step,
                        // to get the value for one step multiply with the step length; error value:
                        // -2^30

                        this.personCapacity,
                        // the total number of persons that can ride in this vehicle

                        this.personNumber
                // the total number of persons which are riding in this vehicle

                );

                // Criar relat�rio auditoria / alertas
                // velocidadePermitida = (double)
                // sumo.do_job_get(Vehicle.getAllowedSpeed(this.idSumoVehicle));
                this.drivingRepport.add(_repport);

                //System.out.println("Data: " + this.drivingRepport.size());
//                System.out.println(this.getIdAuto()+" idAuto = " + this.drivingRepport.get(this.drivingRepport.size() - 1).getAutoID());
//                System.out.println(
//                        this.getIdAuto()+" timestamp = " + this.drivingRepport.get(this.drivingRepport.size() - 1).getTimeStamp());
//                System.out.println("X=" + this.drivingRepport.get(this.drivingRepport.size() - 1).getX_Position() + ", "
//                        + "Y=" + this.drivingRepport.get(this.drivingRepport.size() - 1).getY_Position());
//                System.out.println("speed = " + this.drivingRepport.get(this.drivingRepport.size() - 1).getSpeed());
//                System.out.println(this.getIdAuto()+" odometer = " + this.drivingRepport.get(this.drivingRepport.size() - 1).getOdometer());
                this.setOdometer(this.drivingRepport.get(this.drivingRepport.size() - 1).getOdometer());
//                System.out.println(this.getIdAuto()+" Fuel Consumption = "
//                        + this.drivingRepport.get(this.drivingRepport.size() - 1).getFuelConsumption());
                this.setConsumption(this.drivingRepport.get(this.drivingRepport.size() - 1).getFuelConsumption());
//                System.out.println(this.getIdAuto()+" Fuel Type = " + this.fuelType);
                //System.out.println("Fuel Price = " + this.fuelPrice);

//                System.out.println(
//                        this.getIdAuto()+" CO2 Emission = " + this.drivingRepport.get(this.drivingRepport.size() - 1).getCo2Emission());
                appendToCSV(this.drivingRepport.get(this.drivingRepport.size() - 1), "relatorio.csv", (String) this.sumo.do_job_get(Vehicle.getRouteID(this.idAuto)), "");
                //System.out.println();
                //System.out.println("************************");
                //System.out.println("testes: ");
                //System.out.println("getAngle = " + (double) sumo.do_job_get(Vehicle.getAngle(this.idAuto)));
                //System.out
                //		.println("getAllowedSpeed = " + (double) sumo.do_job_get(Vehicle.getAllowedSpeed(this.idAuto)));
                //System.out.println("getSpeed = " + (double) sumo.do_job_get(Vehicle.getSpeed(this.idAuto)));
                //System.out.println(
                //		"getSpeedDeviation = " + (double) sumo.do_job_get(Vehicle.getSpeedDeviation(this.idAuto)));
                //System.out.println("getMaxSpeedLat = " + (double) sumo.do_job_get(Vehicle.getMaxSpeedLat(this.idAuto)));
                //System.out.println("getSlope = " + (double) sumo.do_job_get(Vehicle.getSlope(this.idAuto))
                //		+ " the slope at the current position of the vehicle in degrees");
                //System.out.println(
                //		"getSpeedWithoutTraCI = " + (double) sumo.do_job_get(Vehicle.getSpeedWithoutTraCI(this.idAuto))
                //				+ " Returns the speed that the vehicle would drive if no speed-influencing\r\n"
                //				+ "command such as setSpeed or slowDown was given.");

                //sumo.do_job_set(Vehicle.setSpeed(this.idAuto, (1000 / 3.6)));
                //double auxspeed = (double) sumo.do_job_get(Vehicle.getSpeed(this.idAuto));
                //System.out.println("new speed = " + (auxspeed * 3.6));
                //System.out.println(
                //		"getSpeedDeviation = " + (double) sumo.do_job_get(Vehicle.getSpeedDeviation(this.idAuto)));
                    sumo.do_job_set(Vehicle.setSpeedMode(this.idAuto, 1));
                    sumo.do_job_set(Vehicle.setMaxSpeed(this.idAuto, 6.8));
                //sumo.do_job_set(Vehicle.setSpeed(this.idAuto, 13.89));

//                System.out.println("getPersonNumber = " + sumo.do_job_get(Vehicle.getPersonNumber(this.idAuto)));
//                //System.out.println("getPersonIDList = " + sumo.do_job_get(Vehicle.getPersonIDList(this.idAuto)));
//
//                System.out.println("************************");
                    }
                }
                
            } else {
                System.out.println("SUMO is closed...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isOn_off() {
        return this.on_off;
    }

    public void setOn_off(boolean _on_off) {
        this.on_off = _on_off;
    }

    public long getAcquisitionRate() {
        return this.acquisitionRate;
    }

    public void setAcquisitionRate(long _acquisitionRate) {
        this.acquisitionRate = _acquisitionRate;
    }

    public String getIdAuto() {
        return this.idAuto;
    }

    public SumoTraciConnection getSumo() {
        return this.sumo;
    }

    public int getFuelType() {
        return this.fuelType;
    }

    public void setFuelType(int _fuelType) {
        if ((_fuelType < 0) || (_fuelType > 4)) {
            this.fuelType = 4;
        } else {
            this.fuelType = _fuelType;
        }
    }

    public double getFuelPrice() {
        return this.fuelPrice;
    }

    public void setFuelPrice(double _fuelPrice) {
        this.fuelPrice = _fuelPrice;
    }

    public SumoColor getColorAuto() {
        return this.colorAuto;
    }

    public int getFuelPreferential() {
        return this.fuelPreferential;
    }

    public void setFuelPreferential(int _fuelPreferential) {
        if ((_fuelPreferential < 0) || (_fuelPreferential > 4)) {
            this.fuelPreferential = 4;
        } else {
            this.fuelPreferential = _fuelPreferential;
        }
    }

    public int getPersonCapacity() {
        return this.personCapacity;
    }

    public int getPersonNumber() {
        return this.personNumber;
    }

    public static void appendToCSV(DrivingData report, String filePath, String routeID, String distance) {
//        double x = 3442.99;
//        double y = 2117.67;
        double[] coordenadasConvertidas = converterCoordenadas(report.getX_Position(), report.getY_Position());
//        System.out.println("Coordenadas convertidas: (" + convertedCoordinates[0] + ", " + convertedCoordinates[1] + ")");
        try (FileWriter writer = new FileWriter(filePath, true)) {
            String line = String.format("%s,%s,%s,%s,%s,%f,%s,%f,%s,%s\n",
                    report.getTimeStamp(),
                    report.getAutoID(),
                    routeID,
                    report.getSpeed(),
                    distance,
                    report.getFuelConsumption(),
                    report.getFuelType(),
                    report.getCo2Emission(),
                    coordenadasConvertidas[0] + "",
                    coordenadasConvertidas[1] + "");

            writer.append(line);
//            System.out.println("Dados do relatório adicionados ao CSV em: " + filePath);
        } catch (IOException e) {
//            System.out.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static void appendToCSV2(String filePath, String linha) {
//        double x = 3442.99;
//        double y = 2117.67;        
//        System.out.println("Coordenadas convertidas: (" + convertedCoordinates[0] + ", " + convertedCoordinates[1] + ")");
/* System.out.println(this.drivingRepport.get(this.drivingRepport.size() - 1).getX_Position()+" "+this.drivingRepport.get(this.drivingRepport.size() - 1).getY_Position()+" -
 Tempo = "+(tempo/1000)+", Odometro = "+this.getOdometer()+", Distancia = "+distanciaPercorrida+", Distancia total = "+distanciaPercorridaTotal+", Velocidade = "+(auxspeed*3.9)+", 
 Consumo = "+(consumomgs)+", Gasto = "+((3.25*consumoL)*(tempo/1000))); */
        try (FileWriter writer = new FileWriter(filePath, true)) {
            String line = String.format("%s\n",
                    linha);

            writer.append(line);
//            System.out.println("Dados do relatório adicionados ao CSV em: " + filePath);
        } catch (IOException e) {
//            System.out.println("Erro ao escrever no arquivo CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @return the odometer
     */
    public double getOdometer() {
        return odometer;
    }

    /**
     * @param odometer the odometer to set
     */
    public void setOdometer(double odometer) {
        this.odometer = odometer;
    }

    /**
     * @return the Consumption
     */
    public double getConsumption() {
        return consumption;
    }

    /**
     * @param consumption the Consumption to set
     */
    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }
}
