const routes = {
  userRecommendations: "/api/users/recommendations",
};

async function fetchMovies(url, params) {
  const response = await axios.get(url, {
    params,
    onDownloadProgress: manageLoadingSpinner("attach"),
  });
  return response.data;
}

function manageLoadingSpinner(action) {
  const $body = $("body");

  if (action === "attach") {
    const $spinner = `
      <div class="spinner-block">
        <div class="spinner"></div>
      </div>
    `;
    $body.append($spinner);
  } else if (action === "detach") {
    const $spinner = $(".spinner-block");

    $spinner.remove();
  }
}

// Handle search form submission
$("#search-form").on("submit", async function handleSearch(evt) {
  evt.preventDefault();

  const age = $("#age").val();
  const gender = $("#gender-select").val();
  const occupation = $("#occupation").val();
  let genres = $("#genres-select").val().join(",");

  const params = {
    age,
    gender,
    occupation,
    genres,
  };

  let recommended;
  
  try {
    recommended = await fetchMovies(
      routes.userRecommendations,
      params
    );
    populateMovies(recommended);

    manageLoadingSpinner("detach");
  } catch (err) {
    console.log(err);
  }
});

// Populate movies list
function populateMovies(response) {
  const $moviesList = $(".movies-list");
  $moviesList.empty();

  for (let obj of response) {
    let { id, movieId, title, genres, imdb, thumbnail } = obj;

    if (!thumbnail) thumbnail = "/assets/images/thumbnail.jpg";

    let $tags = genres.reduce(
      (acc, genre) => (acc += `<span class="tag">${genre}</span>`),
      ""
    );

    let $item = $(
      `
        <a class="card" href=${imdb} target="_blank">
          <div class="card-header">
            <object data="${thumbnail}" type="image/png">
              <img src="/assets/images/thumbnail.jpg" alt="mov-${movieId}" >
            </object>
          </div>
          <div class="card-body">
              <div class="tags">
                  ${$tags}
              </div>
              <h4 class="card-title">
                  ${title}
              </h4>
          </div>
        </a>
      `
    );

    $moviesList.append($item);
  }
}

async function init() {
  $(function () {
    $('#genres-select').multipleSelect({
        multiple: true,
        multipleWidth: 120,
        selectAll: false
    })
  });

  $(function () {
      $('#gender-select').multipleSelect({
          singleRadio: true
      })
  });

  let recommended;
  
  try {
    recommended = await fetchMovies(
      routes.userRecommendations,
      {}
    );
    populateMovies(recommended);

    manageLoadingSpinner("detach");
  } catch (err) {
    console.log(err);
  }
}

init();
