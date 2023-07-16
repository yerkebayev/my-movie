const routes = {
  movieRecommendations: "/api/users/recommendations",
  actionMovies: "/api/users/recommendations?genres=Action",
  dramaMovies: "/api/users/recommendations?genres=Drama",
  animMovies: "/api/users/recommendations?genres=Animation",
};

async function fetchMovies(url) {
  const response = await axios.get(url, {
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

function populateMovies(response, list) {
  const $moviesList = $(`${list} .movies-list`);
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
  let recommended, action, drama, anim;

  try {
    [recommended, action, drama, anim] = await Promise.all([
      fetchMovies(routes.movieRecommendations),
      fetchMovies(routes.actionMovies),
      fetchMovies(routes.dramaMovies),
      fetchMovies(routes.animMovies),
    ]);
      
    populateMovies(recommended, ".recommended-movies");
    populateMovies(action, ".action-movies");
    populateMovies(drama, ".drama-movies");
    populateMovies(anim, ".anim-movies");

    manageLoadingSpinner("detach");
  } catch (err) {
    console.log(err);
  }
}

init();
