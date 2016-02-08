package fr.unice.vicc;


import org.cloudbus.cloudsim.Host;

import java.util.List;

public class EnergyVmAllocationPolicy extends NaiveVmAllocationPolicy {
    public EnergyVmAllocationPolicy(List<? extends Host> list) {
        super(list);
    }

}
