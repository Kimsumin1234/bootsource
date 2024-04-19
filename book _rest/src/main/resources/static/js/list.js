// 제목을 클릭시 a 태그 기능 중지
// data-id 에 있는 값 가져오기
// 부모에 이벤트를 건다
document.querySelector("tbody").addEventListener("click", (e) => {
  e.preventDefault();
  const target = e.target;

  console.log(target.dataset.id);

  // REST 방식중에서 fetch 를 쓰는방법 (화면이 깜빡이지 않고 데이터를 바로 가져올수있다)
  // 변수 대입은 빽팁 활용
  fetch(`http://localhost:8080/read/${target.dataset.id}`)
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      // 디자인 영역 가져와서 value 담기
      document.querySelector("#category").value = data.categoryName;
      document.querySelector("#title").value = data.title;
      document.querySelector("#publisher").value = data.publisherName;
      document.querySelector("#writer").value = data.writer;
      document.querySelector("#price").value = data.price;
      document.querySelector("#salePrice").value = data.salePrice;
      document.querySelector("#book_id").value = data.id;
    });
});

// delete
// 삭제 클릭 시 id 가져오기
const del = document.querySelector(".btn-primary");
console.log(del);
del.addEventListener("click", (e) => {
  e.preventDefault();
  // 위에서 #book_id 에 value를 넣었다
  console.log(document.querySelector("#book_id").value);
  const id = document.querySelector("#book_id").value;
  // /delete/{id} + 데이터
  fetch(`/delete/${id}`, {
    method: "delete", // get 방식이 아니면 method 는 다 쓴다
  })
    .then((response) => response.text())
    .then((data) => {
      if (data == "success") {
        alert("삭제성공");
        location.href = "/book/list?page=1&type=&keyword=";
      }
    });
});

// modify
// create.js 에 있는 코드 복사 떠옴
// form submit 시 submit 기능 중지
const modi = document.querySelector(".btn-secondary");
console.log(modi);
modi.addEventListener("click", (e) => {
  e.preventDefault(); // 태그가 가진 기능(a, submit, reset) 중지

  // form 내용 가져오기 => javascript 객체 생성
  // const myForm = document.querySelector("#myForm");
  // myForm 안에 들어있는 요소 찾기
  // myForm.querySelector("#book_id");
  const book_id = document.querySelector("#book_id").value;
  const data = {
    id: book_id,
    price: document.querySelector("#price").value,
    salePrice: document.querySelector("#salePrice").value,
  };
  console.log(data);

  fetch(`/modify/${book_id}`, {
    method: "put",
    headers: {
      "content-type": "application/json",
    },
    body: JSON.stringify(data),
  })
    .then((response) => response.text())
    .then((data) => {
      if (data == "success") {
        alert("수정성공");
        location.href = "/book/list?page=1&type=&keyword=";
      }
    });
});
