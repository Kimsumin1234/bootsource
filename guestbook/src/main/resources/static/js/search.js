const searchForm = document.querySelector("#searchForm");
const type = document.querySelector("#type");
const keyword = document.querySelector("#keyword");

searchForm.addEventListener("submit", (e) => {
  e.preventDefault();

  if (type.value == "") {
    alert("검색조건을 확인해 주세요");
    type.focus();
    return;
  } else if (keyword.value == "") {
    alert("검색어를 입력해 주세요");
    keyword.focus();
    return;
  }

  e.target.submit();
});
