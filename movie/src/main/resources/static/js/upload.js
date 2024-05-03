// 1. 업로드파일 확장자 검사 function
function checkExtension(fileName) {
  // 정규식 사용
  const regex = /(.*?).(png|gif|jpg)/;

  // ex) txt => false ,  png|gif|jpg => true
  console.log(regex.test(fileName));

  return regex.test(fileName);
}

// 2. 업로드 파일 보여주기 영역 찾기
const uploadResult = document.querySelector(".uploadResult ul");

// 3. 이미지파일 브라우저 화면에 보여주기 function
function showUploadImages(arr) {
  console.log("showUploadImages", arr);

  let tags = "";

  arr.forEach((obj, idx) => {
    console.log(obj.thumbImageURL);
    console.log(obj.imageURL);
    tags += `<li data-name="${obj.fileName}" data-path="${obj.folderPath}" data-uuid="${obj.uuid}">`;
    tags += `<div>`;
    tags += `<a href=""><img src="/upload/display?fileName=${obj.thumbImageURL}" class="block"></a>`;
    tags += `<span class="text-sm d-inline-block mx-1">${obj.fileName}</span>`;
    tags += `<a href="#" data-file="${obj.imageURL}">`;
    tags += `<i class="fa-solid fa-xmark"></i>`;
    tags += `</a>`;
    tags += `</div>`;
    tags += `</li>`;
  });
  uploadResult.insertAdjacentHTML("beforeend", tags);
}

// 4. fileInput 찾기, change 이벤트 걸기 , checkExtension() 호출
// 이미지 파일일경우 FormData() 객체 생성후 append 하기
// fetch() + showUploadImages() 호출하기
const fileInput = document.querySelector("#fileInput");

fileInput.addEventListener("change", (e) => {
  // FileList 로 가져오기 (여러개 파일이 들어올수있으니까)
  const files = e.target.files;
  console.log(files);

  // 스크립트에서 폼 태그 작성
  const formData = new FormData();
  for (let index = 0; index < files.length; index++) {
    // File {name: 'alien2.jpg', lastModified: 1705411448000, ... } : files[index].name 까지 들어간다
    // 이미지 파일만 append 하기 (작성한 폼태그에 파일등록)
    if (checkExtension(files[index].name)) {
      formData.append("uploadFiles", files[index]);
    }
  }

  // 등록한 FileList 를 하나씩 나열하기
  for (const value of formData.values()) {
    console.log(value);
  }

  fetch("/upload/uploadAjax", {
    method: "post",
    headers: {
      "X-CSRF-TOKEN": csrfValue,
    },
    body: formData,
  })
    .then((response) => response.json())
    .then((data) => {
      console.log(data);

      // 이미지 업로드
      showUploadImages(data);
    });
});

// register, modify.html 에서 중복으로 사용해서 upload.js 에 따로 뺌
document.querySelector("#register-form").addEventListener("submit", (e) => {
  e.preventDefault();

  const form = e.target;

  //   첨부파일 정보 수집
  // uploadResult ul li 태그 요소 가져오기
  const attachInfos = document.querySelectorAll(".uploadResult ul li");
  console.log("attachInfos ", attachInfos);

  //   수집된 정보를 폼 태그 자식으로 붙여넣기
  let tags = "";
  attachInfos.forEach((obj, idx) => {
    // <li data-name="wind3.jpg" data-path="2024\04\29" data-uuid="ddeeac10-cdc7-4232-9c5a-d95944af0255">
    // hidden 3개를 => MovieImageDto 객체(private List<MovieImageDto> movieImageDtos = new ArrayList<>();) 하나로 변경 이 작업을 자동으로 해준다
    // 대신에 name 을 적을때 movieImageDtos[${idx}].imgName 같이 변수명을 맞춰줘야 자동으로 해준다 (스프링부트에서 알아서 맵핑 해줌)
    tags += `<input type='hidden' value='${obj.dataset.path}' name='movieImageDtos[${idx}].path'>`;
    tags += `<input type='hidden' value='${obj.dataset.uuid}' name='movieImageDtos[${idx}].uuid'>`;
    tags += `<input type='hidden' value='${obj.dataset.name}' name='movieImageDtos[${idx}].imgName'>`;
  });
  form.insertAdjacentHTML("beforeend", tags);

  console.log(form.innerHTML);

  form.submit();
});
