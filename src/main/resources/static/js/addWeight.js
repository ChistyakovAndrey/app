async function addWeight(event) {
  event.preventDefault();

  const data = {
    weight: $('input[name="weight"]').val(),
    weight_date: $('input[name="weight_date"]').val(),
  };

  let isEverythingOk = true;
   // Проверка валидности weight
    const weightRegex = /^(?!0\d)\d{1,3}(?:.\d{1,2})?$/;
    if (!weightRegex.test(data.weight)) {

      $('.weight_inp').css("border", "4px solid #F37E7E");
      isEverythingOk = false;
    }
  if (isEverythingOk) {
      try {
        const response = await fetch("/add_weight", {
          method: "POST",
          body: JSON.stringify(data),
          headers: {
            "Content-Type": "application/json"
          }
        });
        if (response.ok) {
          window.location.href = '/weight-app';
        } else {
          throw new Error('Ошибка сохранения пользователя');
        }
      } catch (error) {
        console.error(error);
      }
    }
  $('.e1e4gwta0 eml1k9j0 app-catalog-yhwyfr e1gjr6xo0').mouseover(function(){ $('.ep5h2on0 app-catalog-vlma1l e1u4mxfe0').css("display", "none")})
}