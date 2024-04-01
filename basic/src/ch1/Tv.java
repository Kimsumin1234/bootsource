package ch1;

/*
 * 인터페이스 (interface)
 * - 완전 추상화 개념
 * - 부모의 역할로 사용 가능
 * - 추상 메소드만 제공 - 안붙어있지만 public 이 붙어있다
 * - interface 를 implements 하려면 무조건 Override 를 해야한다 (템플릿 강제성)
 * - 그래서 interface 만 알면 implements 한 클래스를 쉽게 파악 할수있다
 * - interface 클래스는 객체 생성 불가
 * - 스프링에서는 인터페이스를 많이 사용한다
 */

public interface Tv {
    void powerOn();

    void powerOff();

    void volumeUp();

    void volumeDown();
}
