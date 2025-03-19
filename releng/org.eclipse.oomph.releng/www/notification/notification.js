function load() {
	const query = new URLSearchParams(window.location.search);

	const color = query.get('color');
	const backgroundColor = query.get('background-color');
	if (color != null && backgroundColor != null) {
		document.body.style.setProperty("--color", color);
		document.body.style.setProperty("--background-color", backgroundColor);
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