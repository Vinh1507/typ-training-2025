package com.bookingcare.enums;

public enum Status {
    DANG_CHO("Đang chờ"),
    DA_XAC_NHAN("Đã xác nhận"),
    DA_HUY("Đã hủy"),
    DA_THANH_TOAN("Đã thanh toán");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Status fromLabel(String label) {
        for (Status status : Status.values()) {
            if (status.getLabel().equalsIgnoreCase(label)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Không tồn tại trạng thái: " + label);
    }
}
