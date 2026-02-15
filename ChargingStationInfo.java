
public class ChargingStationInfo {
    private String name;
    private String[] headTypes; // ประเภทหัวชาร์จที่รองรับ
    private String status = "Available"; // สถานะเริ่มต้น

    public ChargingStationInfo(String name, String[] headTypes) {
        this.name = name;
        this.headTypes = headTypes;
    }

    public String getName() {
        return name;
    }

    public String[] getHeadTypes() {
        return headTypes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
