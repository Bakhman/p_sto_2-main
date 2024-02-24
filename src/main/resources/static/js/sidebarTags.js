let inputValue;

function look(elemId) {
    let elem = document.getElementById(elemId);
    let style = window.getComputedStyle(elem, "");
    elem.style.display = (style.display === 'none') ? 'block' : 'none';
}

function look1(elemId) {
    let elem = document.getElementById(elemId);
    let style = window.getComputedStyle(elem, "");
    elem.style.display = (style.display === 'none') ? 'block' : 'none';
}


document.addEventListener("DOMContentLoaded", async function () {
    viewTagsUserIgnored();
    viewTagsUserTracked();

});

function getTagsValueIgnore() {
    inputValue = document.getElementById("exampleDataList1").value
    getTags();
}

function getTagsValueTracked() {
    inputValue = document.getElementById("exampleDataList").value
    getTags();
}

function getTags() {
    const tagList = document.getElementById('datalistOptions1');
    const tagList1 = document.getElementById('datalistOptions');
    let output = '';
    let url = '/api/user/tag/allTags/filter/' + inputValue;

    fetch(url, {
        headers: {
            'Authorization': `Bearer ${token}`,
        }
        ,
    }).then(res => res.json())
        .then(data => {
            data.forEach(tag => {
                output += `
<option value="${tag.name}"data-id="${tag.id}"></option>
<br>

`
            })
            tagList.innerHTML = output;
            tagList1.innerHTML = output;

        })

}

function getIdIgnoredTeg() {
    let element_input = document.getElementById('exampleDataList1');
    let element_datalist = document.getElementById('datalistOptions1');
    let opSelected = element_datalist.querySelector(`[value="${element_input.value}"]`);
    let id = opSelected.getAttribute('data-id');

    let url = '/api/user/tag/' + id + '/ignored';
    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    })
        .then((response) => response.json())
        .then(() => {
            viewTagsUserIgnored();

        })

}

function getIdTrackedTeg() {
    let element_input = document.getElementById('exampleDataList');
    let element_datalist = document.getElementById('datalistOptions');
    let opSelected = element_datalist.querySelector(`[value="${element_input.value}"]`);
    let id = opSelected.getAttribute('data-id');
    let url = '/api/user/tag/' + id + '/tracked';
    fetch(url, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    })
        .then((response) => response.json())
        .then(() => {
            viewTagsUserTracked();
        })

}

function viewTagsUserIgnored() {
    let tag1 = document.getElementById('result1');
    let output = '';
    let url = '/api/user/tag/ignored';
    fetch(url, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    }).then(res => res.json())
        .then(data => {
            data.forEach(tag => {
                output += `
<p class="tagStyle">${tag.name}<a href="#" onclick="deleteIgnoreTag(${tag.id})">x</a></p>
`
            })
            tag1.innerHTML = output;
        })
}

function viewTagsUserTracked() {
    let tags = document.getElementById('result');
    let output = '';
    let url = '/api/user/tag/tracked';
    fetch(url, {
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    }).then(res => res.json())
        .then(data => {
            data.forEach(tag => {
                output += `

<p class="tagStyle" >${tag.name}<a href="#" onclick="deleteTrackTag(${tag.id})">x</a></p>

`
            })
            tags.innerHTML = output;
        })

}

function deleteIgnoreTag(id) {
    let url = '/api/user/tag/' + id + '/ignore/delete';
    fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    })
        .then((response) => response.json())
    viewTagsUserIgnored();
}

function deleteTrackTag(id) {
    let url = '/api/user/tag/' + id + '/track/delete';
    fetch(url, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`,
        },
    })
        .then((response) => response.json())
    viewTagsUserTracked();


}

