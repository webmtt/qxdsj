package cma.cimiss2.dpc.indb.core.ots;

import java.util.List;
import java.util.Map;

/**
 * Created by yizheng on 2018/5/3.
 */
public class OTSBatchResult {

    public static class FailedRowResult {
        private Map<String, Object> row;
        private Exception exception;

        public Map<String, Object> getRow() {
            return row;
        }

        public void setRow(Map<String, Object> row) {
            this.row = row;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }
    }

    private int successRowCount;
    private int failedRowCount;
    private List<FailedRowResult> failedRows;

    public int getSuccessRowCount() {
        return successRowCount;
    }

    public void setSuccessRowCount(int successRowCount) {
        this.successRowCount = successRowCount;
    }

    public int getFailedRowCount() {
        return failedRowCount;
    }

    public void setFailedRowCount(int failedRowCount) {
        this.failedRowCount = failedRowCount;
    }

    public List<FailedRowResult> getFailedRows() {
        return failedRows;
    }

    public void setFailedRows(List<FailedRowResult> failedRows) {
        this.failedRows = failedRows;
    }
}
