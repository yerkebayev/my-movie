const routes = {
  movieRecommendations: "/api/movies/recommendations",
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

$("#search-form").on("submit", async function handleSearch(evt) {
  evt.preventDefault();

  const title = $("#title").val();
  const limit = $("#limit").val();

  const params = {
    title,
    limit,
  };

  try {
    recommended = await fetchMovies(
      routes.movieRecommendations,
      params
    );
    populateMovies(recommended);

    manageLoadingSpinner("detach");
  } catch (err) {
    console.log(err);
  }
});

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
  let recommended;
  
  try {
    recommended = await fetchMovies(
      routes.movieRecommendations,
      {}
    );
    populateMovies(recommended);

    manageLoadingSpinner("detach");
  } catch (err) {
    console.log(err);
  }
}

init();
