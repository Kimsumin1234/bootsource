package ch1;

public class SamsungTv implements Tv {

    @Override
    public void powerOn() {
        System.out.println("SamsungTv - 전원 On");
    }

    @Override
    public void powerOff() {
        System.out.println("SamsungTv - 전원 Off");
    }

    @Override
    public void volumeUp() {
        System.out.println("SamsungTv - volume up");
    }

    @Override
    public void volumeDown() {
        System.out.println("SamsungTv - volume down");
    }

}
