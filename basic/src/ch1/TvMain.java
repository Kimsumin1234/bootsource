package ch1;

public class TvMain {
    public static void main(String[] args) {
        // tv 객체 생성
        // - 1번 방법 (new)
        // LgTv lgTv = new LgTv();
        // SamsungTv samsungTv = new SamsungTv();

        // - 2번 방법 (왼쪽은 부모 오른쪽은 자식)
        // - 2번 방법을 많이 사용한다 SamsungTv 를 사용하고 싶으면 오른쪽 자식만 바꾸면되서 (나머지는 오버라이딩이라 비슷해서)
        // Tv tv = new LgTv();

        // 생성자를 이용
        // 다형성 : LgTv에 부모 Speaker 생성자를 만들어놔서 자식 스피커가 아무거나 들어와도된다
        Tv tv = new LgTv(new BritzSpeaker());
        Tv tv2 = new LgTv(new IriverSpeaker());

        // Tv tv3 = new LgTv(); 이거는 빨간줄이 뜬다 부모의 범위가 적어서
        // 자식에 생성된 setSpeaker 접근범위가 안된다
        LgTv tv3 = new LgTv();
        tv3.setSpeaker(new BritzSpeaker());

        tv.powerOn();

        // NullPointerException: Cannot invoke "ch1.BritzSpeaker.volumeUp()" because
        // "this.britzSpeaker" is null
        tv.volumeUp();

        tv.volumeDown();
        tv.powerOff();
    }
}
