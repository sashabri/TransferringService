package com.example.transferringservice.model;

public class Operation {
   private String operationId;

   private String code;

   private String numCardFrom;

   private String numCardTo;

   private Integer sum;

   private boolean success;

   public Operation(String operationId, String code, String numCardFrom, String numCardTo, Integer sum, boolean success) {
      this.operationId = operationId;
      this.code = code;
      this.numCardFrom = numCardFrom;
      this.numCardTo = numCardTo;
      this.sum = sum;
      this.success = success;
   }

   public Operation() {
   }

   public void setOperationId(String operationId) {
      this.operationId = operationId;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public void setNumCardFrom(String numCardFrom) {
      this.numCardFrom = numCardFrom;
   }

   public void setNumCardTo(String numCardTo) {
      this.numCardTo = numCardTo;
   }

   public void setSum(Integer sum) {
      this.sum = sum;
   }

   public void setSuccess(boolean success) {
      this.success = success;
   }

   public String getOperationId() {
      return operationId;
   }

   public String getCode() {
      return code;
   }

   public String getNumCardFrom() {
      return numCardFrom;
   }

   public String getNumCardTo() {
      return numCardTo;
   }

   public Integer getSum() {
      return sum;
   }

   public boolean isSuccess() {
      return success;
   }
}
