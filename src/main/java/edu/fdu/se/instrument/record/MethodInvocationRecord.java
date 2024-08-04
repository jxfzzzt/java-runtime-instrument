package edu.fdu.se.instrument.record;

import edu.fdu.se.instrument.execption.InstrumentException;
import edu.fdu.se.instrument.state.StateNode;

import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MethodInvocationRecord {

    private Map<Integer, MethodInvocation> invocationMap;

    private List<MethodInvocation> MethodInvocationSeq;

    private Set<String> methodExecuteSigSet;

    private Set<MethodInvocation> methodInvocationSet;

    private Map<String, StateNode> stateNodeMap;

    private Map<Integer, String> methodInvocationMap;


    public MethodInvocationRecord(final ClassLoader loader, Map<String, StateNode> stateNodeMap,
                                  Set<String> methodExecuteSigSet, Map<Integer, String> methodInvocationMap) {
        this.methodExecuteSigSet = methodExecuteSigSet;
        this.methodInvocationMap = methodInvocationMap;
        this.stateNodeMap = stateNodeMap;
        checkInvocationIdValid(methodInvocationMap.keySet());
    }

    private void checkInvocationIdValid(Set<Integer> invocationIdList) {
        int n = invocationIdList.size();
        for (int i = 1; i <= n; i++) {
            if (!invocationIdList.contains(i)) {
                throw new InstrumentException("");
            }
        }
    }

    private void processMethodInvocationSeq() {

    }

    public boolean containsMethod(MethodInvocation invocation) {
        return false;
    }

    public Set<String> getMethodExecuteSigSet() {
        return methodExecuteSigSet;
    }
}
