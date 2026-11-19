(function () {
  function apply() {
    var marker = document.querySelector(".emotional-jenkins[data-image]");
    var panel = document.getElementById("side-panel");
    if (!marker || !panel || panel.querySelector(".emotional-jenkins-figure")) {
      return;
    }
    var img = document.createElement("img");
    img.className = "emotional-jenkins-figure";
    img.src = marker.dataset.image;
    img.alt = "Mr. Jenkins is " + marker.dataset.mood;
    img.title = img.alt;
    panel.appendChild(img);
  }
  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", apply);
  } else {
    apply();
  }
})();
