package io.sim;

import java.io.IOException;

import de.tudresden.sumo.objects.SumoColor;
import it.polito.appeal.traci.SumoTraciConnection;

public class EnvSimulator extends Thread {

    private SumoTraciConnection sumo;

    public EnvSimulator() {
        
    }

    public void run() {

        /* SUMO */
        String sumo_bin = "sumo-gui";
        String config_file = "map/map.sumo.cfg";

        // Sumo connection
        this.sumo = new SumoTraciConnection(sumo_bin, config_file);
        sumo.addOption("start", "1"); // auto-run on GUI show
        sumo.addOption("quit-on-end", "1"); // auto-close on end-

//        try {
//            sumo.runServer(12345);
//
////            Itinerary i12 = new Itinerary("15469696#4 15240255#3 15240255#4 795084572#0 795084572#1 795084572#2 795084572#3 795084572#4 795084572#5 794061354#0 794061354#1 794061354#2 794061354#3 794061354#4 794061354#5 794061354#6 794061354#7 134216629#0 134216629#1 134216629#2 134216629#3 134216629#4 134216629#5 134216629#6 134216629#7 134216629#8 134216629#9 134216629#10 72145316#0 72145316#1 72145316#2 72145316#3 72145316#4 72145316#5 72145316#6 72145316#7 72145316#8 72145316#9 72145316#10 72145316#11 72145316#12 72145316#13 72145316#14 799484670#0 799484670#1 331159750#0 331159750#1 331159750#2 331159750#3 331159750#4 783216748#0 783216748#1 15017128#0 15017128#1 15017128#2 15017128#3 15017128#4", "0");
//            Itinerary i1 = new Itinerary("75035877 207143229#1 207143229#2 207143229#3 207143229#4 207143229#5 207143229#6 207143229#7 207143229#8 207143229#9 207143229#10 794061358#0 794061358#1 795094238 30969387#0 30969387#1 30969387#2 30969387#3 30969387#4 42161072#0 42161072#1 787840539#2 234509633#0 234509633#1 42161338 622500513#0 622500513#1 622500513#2 622500513#3 622500513#4 622500513#5 622500513#6 42161336#0 -42161336#0", "1");
//            
//            if (i1.isOn()) {
//
//                // fuelType: 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
//                int fuelType = 2;
//                int fuelPreferential = 2;
//                double fuelPrice = 3.40;
//                int personCapacity = 1;
//                int personNumber = 1;
//                SumoColor green = new SumoColor(0, 255, 0, 126);
//                SumoColor red = new SumoColor(255, 0, 0, 126);
//
//                Auto a1 = new Auto(true, "CAR1s", red, "D1", sumo, 500, fuelType, fuelPreferential, fuelPrice, personCapacity, personNumber);
//
//                TransportService tS1 = new TransportService(true, "CAR1", i1, a1, sumo);
//                System.out.println(i1.getItinerary()[1]);
//                //tS1.start();
//                Thread.sleep(5000);
//                a1.start();
//            }
////            if (i12.isOn()) {
////
////                // fuelType: 1-diesel, 2-gasoline, 3-ethanol, 4-hybrid
////                int fuelType = 2;
////                int fuelPreferential = 2;
////                double fuelPrice = 3.40;
////                int personCapacity = 1;
////                int personNumber = 1;
////                SumoColor green = new SumoColor(0, 255, 0, 126);
////                SumoColor red = new SumoColor(255, 0, 0, 126);
////
////                Auto a12 = new Auto(true, "CAR2", red, "D2", sumo, 500, fuelType, fuelPreferential, fuelPrice, personCapacity, personNumber);
////
////                TransportService tS12 = new TransportService(true, "CAR2", i12, a12, sumo);
////                System.out.println(i12.getItinerary()[1]);
////                tS12.start();
////                Thread.sleep(5000);
////                a12.start();
////            }
//
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

    }

}
