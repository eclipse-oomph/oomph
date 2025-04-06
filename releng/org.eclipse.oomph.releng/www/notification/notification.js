function generate() {
	try {
		const generators = document.querySelectorAll('[data-generate]');
		for (const element of generators) {
			const generator = element.getAttribute('data-generate');
			const generate = new Function(generator);
			generate.call(element, element);
		}
	} catch (exception) {
		document.body.prepend(...toElements(`<span>Failed to generate content: <span><b style="color: FireBrick">${exception.message}</b><br/>`));
		console.log(exception);
	}
}

function load() {
	const query = new URLSearchParams(window.location.search);

	const color = query.get('color');
	const backgroundColor = query.get('background-color');
	if (color != null && backgroundColor != null) {
		document.body.style.setProperty("--color", color);
		document.body.style.setProperty("--background-color", backgroundColor);
	} else {
		document.body.style.setProperty("--color", "black");
		document.body.style.setProperty("--background-color", "white");
	}

	if (query.get('show-table') == 'true') {
		const queryTable = document.getElementById('query');
		if (queryTable != null) {
			queryTable.parentElement.style.display = 'table';

			const lastElement = queryTable.lastElementChild;
			for (const [key, value] of query) {
				const row = lastElement.cloneNode(true);
				const propertyTD = row.children.item(0);
				propertyTD.innerText = key
				const valueTD = row.children.item(1);
				valueTD.innerText = value
				queryTable.appendChild(row);
			}
			lastElement.remove();
		}
	}

	for (const element of document.querySelectorAll('.copy-query')) {
		const url = new URL(element.href);
		for (let [k, v] of new URLSearchParams(window.location.search).entries()) {
			if (!url.searchParams.has(k)) {
				url.searchParams.set(k, v)
			}
		}
		element.href = url.toString();
	}
}

function getQueryParameter(id, defaultValue) {
	const location = new URL(window.location);
	const search = new URLSearchParams(location.search);
	const result = search.get(id);
	return result ?? defaultValue;
}

function updateInnerInnerHTML(selector, value) {
	const versions = document.querySelectorAll(selector);
	for (const element of versions) {
		element.innerText = value;
	}
}

function toElements(text) {
	const wrapper = document.createElement('div');
	wrapper.innerHTML = text;
	return wrapper.children
}
