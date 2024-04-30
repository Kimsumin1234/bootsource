// 검색 창에서 엔터를 치면 이벤트 발생
// 검색어를 가지고 온 후 searchForm 안 keyword 요소 value 값 저장
// searchForm 전송
document.querySelector("[name='keyword']").addEventListener("keyup", (e) => {
  // 엔터가 눌리면 이벤트 발생
  if (e.keyCode == 13) {
    // alert("엔터"); 엔터를 누르면 이벤트가 잘 발생하는지 확인

    // 검색어 가져오기
    const keyword = e.target.value;
    if (!keyword) {
      alert("검색어를 입력해 주세요");
      return;
    }
    const searchForm = document.querySelector("#searchForm");
    searchForm.querySelector("[name='keyword']").value = keyword;

    // searchForm 에 value 가 잘 들어왔는지 f12 로 확인
    // console.log(searchForm);

    searchForm.submit();
  }
});
