package fr.unice.vicc;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.Comparator;

/**
 * Created by yukikoo on 2/8/16.
 */
public class EnergyHostAvailabilityComparator implements Comparator<Host> {
    @Override
    public int compare(Host host, Host t1) {


        PowerHost powerHost1 = (PowerHost) host;
        PowerHost powerHost2 = (PowerHost) t1;

        if(powerHost1.getMaxPower() > powerHost2.getMaxPower()){
            return 1;
        }else if(powerHost1.getMaxPower() < powerHost2.getMaxPower()){
            return -1;
        }

        double mipsAvailable1 = host.getAvailableMips();
        double mipsAvailable2 = t1.getAvailableMips();

        int ram1 = host.getRamProvisioner().getAvailableRam();
        int ram2 = t1.getRamProvisioner().getAvailableRam();

        double comparison = mipsAvailable1 - mipsAvailable2;
        int ramComparison = ram1 - ram2;

        if(comparison == 0){
            if(ramComparison == 0) {
                return 0;
            }else if(ramComparison < 0){
                return -1;
            }else{
                return 1;
            }
        }else if(comparison < 0){
            return -1;
        }else{
            return 1;
        }


    }
}

