async function submitForm(event) {
  event.preventDefault();
  const genderRadios = $('input[name="gender"]');
  let selectedGender;

  for (let i = 0; i < genderRadios.length; i++) {
    if (genderRadios[i].checked) {
      selectedGender = genderRadios[i].value;
      break;
    }
  }

  const data = {
    username: $('input[name="username"]').val(),
    name: $('input[name="name"]').val(),
    age: $('input[name="age"]').val(),
    password: $('input[name="password"]').val(),
    email: $('input[name="email"]').val(),
    gender: selectedGender
  };

  let isEverythingOk = true;
  let usernameDoesNotExist = true;
   // Проверка валидности username
    const usernameRegex = /^[a-zA-Z0-9]{4,}$/;
    if (!usernameRegex.test(data.username)) {
      $('.usn_inp').css("border", "4px solid #F37E7E");
      isEverythingOk = false;
    } else {
        try {
          const response = await fetch("/api/doesUserExist", {
            method: "POST",
            body: JSON.stringify({ username: data.username }),
            headers: {
              "Content-Type": "application/json"
            }
          });
          const userExists = await response.json();

          if (userExists) {
            usernameDoesNotExist = false;
            isEverythingOk = false;
            $('.usn_inp').css("border", "4px solid #F37E7E");
            $('.err_msg').text("Пользователь " + $('input[name="username"]').val() + " уже существует");
            $('.err_msg').animate({ "opacity": "1" }, 300);
          } else {
            $('.usn_inp').css("border", "4px solid #2B6103");
            $('.err_msg').text(" ");
            $('.err_msg').css("opacity", "0");
          }
        } catch (error) {
          isEverythingOk = false;
          console.log("Error:", error.message);
        }
    }

  // Проверка валидности name
  const nameRegex = /^[a-zA-Zа-яА-Я]{2,}$/;
  if (!nameRegex.test(data.name)) {
    $('.name_inp').css("border", "4px solid #F37E7E");
    isEverythingOk = false;
  } else {
    $('.name_inp').css("border", "4px solid #2B6103");
  }

  // Проверка валидности возраста
//  const age = parseInt(data.age);
//  if (isNaN(age) || age < 1 || age > 100) {
//    $('.age_inp').css("border", "4px solid #F37E7E");
//    isEverythingOk = false;
//  } else {
//    $('.age_inp').css("border", "4px solid #2B6103");
//  }

  // Проверка валидности email
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(data.email)) {
    $('.mail_inp').css("border", "4px solid #F37E7E");
    isEverythingOk = false;
  } else {
     try {
              const response = await fetch("/api/doesEmailExist", {
                method: "POST",
                body: JSON.stringify({ email: data.email }),
                headers: {
                  "Content-Type": "application/json"
                }
              });
              const emailExists = await response.json();

              if (emailExists) {
                isEverythingOk = false;
                $('.mail_inp').css("border", "4px solid #F37E7E");
                $('.err_msg').text("Адрес электронной почты уже используется");
                $('.err_msg').animate({ "opacity": "1" }, 300);
              } else {
              $('.mail_inp').css("border", "4px solid #2B6103");
              if(usernameDoesNotExist){

                              $('.err_msg').text("");
                              $('.err_msg').css("opacity", "0");
              }
              }
            } catch (error) {
              isEverythingOk = false;
              console.log("Error:", error.message);
            }
  }

  // Проверка валидности пароля
  const passwordRegex = /^[a-zA-Z0-9!?_]{4,}$/;
  if (!passwordRegex.test(data.password)) {
    $('.pass_inp').css("border", "4px solid #F37E7E");
    isEverythingOk = false;
  } else {
    $('.pass_inp').css("border", "4px solid #2B6103");
  }
  if (isEverythingOk) {
      try {
        const response = await fetch("/api/addUser", {
          method: "POST",
          body: JSON.stringify(data),
          headers: {
            "Content-Type": "application/json"
          }
        });
        if (response.ok) {
          window.location.href = '/login';
        } else {
          throw new Error('Ошибка сохранения пользователя');
        }
      } catch (error) {
        console.error(error);
      }
    }
}