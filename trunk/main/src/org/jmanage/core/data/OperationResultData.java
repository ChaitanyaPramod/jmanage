package org.jmanage.core.data;

/**
 *
 * date:  Jan 23, 2005
 * @author	Rakesh Kalra
 */
public class OperationResultData {

    public static final int RESULT_OK = 0;
    public static final int RESULT_ERROR =1;

    private Object output;
    private int result = RESULT_OK;
    private String errorString;

    /** default ctor */
    public OperationResultData(){}

    public OperationResultData(Object output, int result, String errorString){
        this.setOutput(output);
        this.setResult(result);
        this.setErrorString(errorString);
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }
}
