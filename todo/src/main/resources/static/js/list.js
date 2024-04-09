// 체크박스 클릭시 id 가져오기
// value 는 form, checkbox, select/option ... 에 줄수있다

// 첫번째 방법
// 화면의 중복 요소에 이벤트 작성
// document.querySelectorAll("input[type='checkbox']").addEventListener("click")

// 두번째 방법
// 이벤트 전파 => 부모요소가 감지(실제 이벤트는 자식요소가 일어남)
document.querySelector(".list-group").addEventListener("click", (e) => {
  console.log("이벤트가 발생한 대상" + e.target);
  console.log("이벤트가 발생한 대상 value " + e.target.value);
  console.log("이벤트를 감지한 대상" + e.currentTarget);

  const form = document.querySelector("#completedForm");
  form.querySelector("input[name='id']").value = e.target.value;
  form.submit();

  // get 방식으로 value 보내기
  // location.href = "/todo/update?id=" + e.target.value;

  // post 방식으로 value 보내기 : form 태그 만들기, fetch 구문 사용하기
});
