package fr.unice.vicc;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yukikoo on 2/8/16.
 */
public class GreedyVmAllocationPolicy extends VmAllocationPolicy {

    private Map<Vm,Host> hoster;

    public GreedyVmAllocationPolicy(List<? extends Host> list) {
        super(list);
        hoster =new HashMap<>();
    }

    @Override
    protected void setHostList(List<? extends Host> hostList) {
        super.setHostList(hostList);
        hoster = new HashMap<>();
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        List<Host> hosts = getHostList();
        hosts.sort(new EnergyHostAvailabilityComparator());
        for (Host host : hosts) {


            if (!isSuitable(vm, host)) {
                continue;
            }

            if (host.vmCreate(vm)) {
                hoster.put(vm, host);
                return true;
            }
        }
        return false;
    }

    private int heuristiqueMips = 550;
    private int heuristiqueRam = 220;
    private int heuristiqueBW = 30000;

    public boolean isSuitable(Vm vm, Host host){
        return host.getVmScheduler().getPeCapacity() + heuristiqueMips >= vm.getCurrentRequestedMaxMips() &&
                host.getVmScheduler().getAvailableMips() + heuristiqueMips >= vm.getCurrentRequestedTotalMips()&&
                host.getRamProvisioner().getAvailableRam() + heuristiqueRam >= vm.getCurrentRequestedRam() &&
                host.getBwProvisioner().getAvailableBw() +heuristiqueBW >=  vm.getCurrentRequestedBw();
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {


        if(!host.isSuitableForVm(vm)){
            return false;
        }

        if (host.vmCreate(vm)) {
            hoster.put(vm, host);
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> list) {

        return null;
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
