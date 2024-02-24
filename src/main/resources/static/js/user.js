async function changePage(page) {
    let cookieValue = document.cookie.replace(/(?:(?:^|.*;\s*)token\s*\=\s*([^;]*).*$)|^.*$/, "$1");
    let searchInput = document.getElementById("search-input").value;
    let itemsOnPage = 16;
    const url = 'http://localhost:8091/api/user/new?currentPageNumber=' + page +
                                                    '&itemsOnPage=' + itemsOnPage +
                                                    '&filter=' + searchInput;

    async function getApi(url) {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + cookieValue,
            },
        });
        let data = JSON.parse(await response.text())
        console.log(data);
        if (response) {
            hideLoader();
        }
        totalPage = data["totalPageCount"];
        show(data);
    }

    getApi(url);


    function hideLoader() {
        document.querySelector('.loading').style.display = 'none';
    }

    function show(data) {
        let divInner = document.createElement('div');
        divInner.className = 'row mb-4';

        for (let r of data.items) {
            // Create div tag User profile
            let divCol = document.createElement('div');
            divCol.className = 'col col-md-3 mb-4';

            let divCard = document.createElement('div');
            divCard.className = 'card shadow-sm';

            let divUserProfile = document.createElement('div');
            divUserProfile.className = 'card-body';

            // Create div tag where is an image User
            let divImage = document.createElement('div');
            divImage.className = 'image';

            // Create img tag
            let image = document.createElement('img');
            image.src = r.linkImage;
            image.alt = r.fullName + ' Image';
            image.width = 48;
            image.height = 48;
            image.align = 'left';
            image.className = 'rounded d-block';

            // Create div tag User info
            let divUser = document.createElement('div');
            divUser.className = 'user d-flex flex-column';

            let divUserName = document.createElement('div');
            divUserName.className = 'username';
            let hrefUser = document.createElement('a');
            hrefUser.innerText = r.fullName;
            hrefUser.href = '/user/' + r.id;
            hrefUser.className = 'fs-2';
            divUserName.appendChild(hrefUser);

            let divLocation = document.createElement('div');
            divLocation.className = 'user-location';
            let spanLocation = document.createElement('span')
            spanLocation.innerText = r.city;
            let iLocation = document.createElement('i');
            iLocation.className = 'bi bi-geo-alt-fill';
            divLocation.appendChild(iLocation);
            divLocation.appendChild(spanLocation);

            let divReputation = document.createElement('div');
            divReputation.className = 'reputation';
            let spanReputation = document.createElement('span');
            spanReputation.innerText = r.reputation;
            let iReputation = document.createElement('i');
            iReputation.className = 'bi bi-star-fill';
            divReputation.appendChild(iReputation);
            divReputation.appendChild(spanReputation);

            divUser.appendChild(divUserName);
            divUser.appendChild(divLocation);
            divUser.appendChild(divReputation);
            divImage.appendChild(image);
            divUserProfile.appendChild(divImage);
            divUserProfile.appendChild(divUser);
            divCard.appendChild(divUserProfile);
            divCol.appendChild(divCard);
            divInner.appendChild(divCol);
        }
        document.querySelector(".users").innerHTML = divInner.outerHTML;
    }
}

document.getElementById('search-input').addEventListener('input', (e) => {
    changePage(1);
})