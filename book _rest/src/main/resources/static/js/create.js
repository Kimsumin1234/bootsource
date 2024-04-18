// form submit 시 submit 기능 중지
const form = document.querySelector("form[method='post']");
console.log(form);
form.addEventListener("submit", (e) => {
  e.preventDefault(); // 태그가 가진 기능(a, submit, reset) 중지

  // form 내용 가져오기 => javascript 객체 생성
  const data = {
    categoryName: document.querySelector("select[name='categoryName']").value,
    title: document.querySelector("#title").value,
    publisherName: document.querySelector("select[name='publisherName']").value,
    writer: document.querySelector("#writer").value,
    price: document.querySelector("#price").value,
    salePrice: document.querySelector("#salePrice").value,
  };
  console.log(data);

  // fetch 함수는 method 를 지정 안하면 get 으로 전송됨
  // JSON.stringify() : javascript 객체 => json 타입으로 변환
  fetch(`http://localhost:8080/book/new`, {
    method: "post",
    headers: {
      // 포스트맨 Headers 에 Content-type 을 보고 적는다
      "content-type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => response.text()) // return new ResponseEntity<>("success", HttpStatus.OK) "success" 가 String 이여서 .text() 를 사용
    .then((data) => {
      if (data == "success") alert("입력성공");
      location.href = "/book/list?page=1&type=&keyword=";
    });
});
