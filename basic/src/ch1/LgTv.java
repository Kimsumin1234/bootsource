package ch1;

public class LgTv implements Tv {
    /**
     * 멤버변수
     * 
     * - 기본형 : int, long, boolean ...
     * 1) 정수타입 : 0
     * 2) 불린타입 : false
     * 3) 실수타입 : 0.0
     * 
     * - 참조형(대문자로 시작하는것들(클래스명,String ...), 배열) : null 로 초기화
     * 
     * 멤버 변수 초기화 방법
     * - 포함관계 일 경우
     * 1) setter 메소드 이용
     * 2) 생성자 이용
     */

    // Speaker 달기
    // 멤버 변수 초기화
    /**
     * 멤버 변수 선언만 해서는 null 값이라 메소드 호출하면 에러가 뜬다
     * private BritzSpeaker britzSpeaker; // == private BritzSpeaker britzSpeaker; =
     * null;
     * 
     */
    private Speaker speaker;

    // 생성자
    public LgTv(Speaker speaker) {
        this.speaker = speaker;
    }

    public LgTv() {
    }

    @Override
    public void powerOn() {
        System.out.println("LgTv - 전원 On");
    }

    @Override
    public void powerOff() {
        System.out.println("LgTv - 전원 Off");
    }

    @Override
    public void volumeUp() {
        // System.out.println("LgTv - volume up");
        // BritzSpeaker 의 볼륨 사용하기

        // Cannot invoke "ch1.BritzSpeaker.volumeUp()"because"this.britzSpeaker"is null
        speaker.volumeUp();
    }

    @Override
    public void volumeDown() {
        // System.out.println("LgTv - volume down");
        // BritzSpeaker 의 볼륨 사용하기
        speaker.volumeDown();
    }

    // setter
    public void setSpeaker(Speaker speaker) {
        this.speaker = speaker;
    }

}
