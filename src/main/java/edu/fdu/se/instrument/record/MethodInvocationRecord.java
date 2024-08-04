package edu.fdu.se.instrument.record;

import edu.fdu.se.instrument.execption.InstrumentException;
import edu.fdu.se.instrument.state.StateNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class MethodInvocationRecord {

    private List<String> methodInvokeSequence;

    private Set<String> methodExecuteSet;

    private Map<String, StateNode> stateNodeMap;


    public MethodInvocationRecord(final ClassLoader loader, Map<String, StateNode> stateNodeMap,
                                  Set<String> methodExecuteSigSet, Map<Integer, String> methodInvokeMap) {
        this.methodExecuteSet = methodExecuteSigSet;
        this.stateNodeMap = stateNodeMap;

        checkAndSetInvokeSequence(methodInvokeMap);
    }

    private void checkAndSetInvokeSequence(Map<Integer, String> methodInvokeMap) {
        Set<Integer> invokeIdList = methodInvokeMap.keySet();

        int n = invokeIdList.size();
        for (int i = 1; i <= n; i++) {
            if (!invokeIdList.contains(i)) {
                throw new InstrumentException("Invalid invocation ID: expected ID " + i + " is missing.");
            }
        }

        this.methodInvokeSequence = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            this.methodInvokeSequence.add(methodInvokeMap.get(i));
        }
    }

    public boolean containsMethod(String methodSignature) {
        return this.methodExecuteSet.contains(methodSignature);
    }

    public List<String> getMethodInvokeSequence() {
        return methodInvokeSequence;
    }

    public Map<String, StateNode> getStateNodeMap() {
        return stateNodeMap;
    }

    public Set<String> getMethodExecuteSet() {
        return methodExecuteSet;
    }
}
