package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;
import org.cloudbus.cloudsim.power.PowerHost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class EnergyVmAllocationPolicy extends VmAllocationPolicy {
    /**
     * The map to track the server that host each running VM.
     */
    private Map<Vm, Host> hoster;

    public EnergyVmAllocationPolicy(List<? extends Host> list) {
        super(list);
        hoster = new HashMap<>();
    }

    @Override
    protected void setHostList(List<? extends Host> hostList) {
        super.setHostList(hostList);
        hoster = new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {
        return null;
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {

        List<Host> hosts = getHostList();
        hosts.sort(new EnergyHostAvailabilityComparator());
        for (Host host : hosts) {

            if (host.vmCreate(vm)) {
                hoster.put(vm, host);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {


        PowerHost powerHost = (PowerHost) host;
        if(powerHost.getMaxPower() == 137.0){
            return false;
        }

        if (host.vmCreate(vm)) {
            hoster.put(vm, host);
            return true;
        }
        return false;
    }

    @Override
    public void deallocateHostForVm(Vm vm) {

        Host host = getHost(vm);
        host.vmDestroy(vm);
    }

    @Override
    public Host getHost(Vm vm) {
        return vm.getHost();
    }

    @Override
    public Host getHost(int vmId, int userId) {
        for (Vm vm : hoster.keySet()) {
            if (vm.getId() == vmId && vm.getUserId() == userId) {
                return vm.getHost();
            }
        }
        return null;
    }

}
