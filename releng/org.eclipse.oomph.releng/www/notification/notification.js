const scriptBase = new URL(".", document.currentScript.src).href;

const productNames = new Map([
	['org.eclipse.epp.package.committers.product', 'Eclipse IDE for Eclipse Committers'],
	['org.eclipse.epp.package.cpp.product', 'Eclipse IDE for C/C++ Developers'],
	['org.eclipse.epp.package.dsl.product', 'Eclipse IDE for Java and DSL Developers'],
	['org.eclipse.epp.package.embedcpp.product', 'Eclipse IDE for Embedded C/C++ Developers'],
	['org.eclipse.epp.package.java.product', 'Eclipse IDE for Java Developers'],
	['org.eclipse.epp.package.jee.product', 'Eclipse IDE for Enterprise Java and Web Developers'],
	['org.eclipse.epp.package.modeling.product', 'Eclipse Modeling Tools'],
	['org.eclipse.epp.package.php.product', 'Eclipse IDE for PHP Developers'],
	['org.eclipse.epp.package.rcp.product', 'Eclipse IDE for RCP and RAP Developers'],
	['org.eclipse.epp.package.scout.product', 'Eclipse IDE for Scout Developers'],
	['org.eclipse.sdk.ide', 'Eclipse SDK'],
	['default', 'Eclipse IDE'],
]);

function getProductName() {
	return productNames.get(getQueryParameter('product-id') ?? 'default')
		?? getQueryParameter('product-id')
		?? 'Eclipse IDE';
}

const imagesPrefix = 'https://www.eclipse.org/downloads/images/';

const productImages = new Map([
	['org.eclipse.epp.package.committers.product', `${imagesPrefix}committers.png`],
	['org.eclipse.epp.package.cpp.product', `${imagesPrefix}cdt.png`],
	['org.eclipse.epp.package.dsl.product', `${imagesPrefix}dsl-package_42.png`],
	['org.eclipse.epp.package.embedcpp.product', `${imagesPrefix}cdt.png`],
	['org.eclipse.epp.package.java.product', `${imagesPrefix}java.png`],
	['org.eclipse.epp.package.jee.product', `${imagesPrefix}javaee.png`],
	['org.eclipse.epp.package.modeling.product', `${imagesPrefix}modeling.png`],
	['org.eclipse.epp.package.php.product', `${imagesPrefix}php.png`],
	['org.eclipse.epp.package.rcp.product', `${imagesPrefix}rcp.png`],
	['org.eclipse.epp.package.scout.product', `${imagesPrefix}scout.jpg`],
	['org.eclipse.sdk.ide', `${imagesPrefix}committers.png`],
	['default', `${imagesPrefix}committers.png`],
]);

function getProductImage() {
	return productImages.get(getQueryParameter('product-id') ?? 'default')
		?? productImages.get('default')
}

function generate(showLogos) {
	try {
		const generators = document.querySelectorAll('[data-generate]');
		for (const element of generators) {
			const generator = element.getAttribute('data-generate');
			const generate = new Function(generator);
			generate.call(element, element);
		}
		const ideWG = document.getElementById('ide-wg');
		if (ideWG != null && (showLogos || getQueryParameter('logos', 'false') == 'true')) {
			genenerateLogos(ideWG);
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

async function genenerateLogos(element) {
	fetch('https://membership.eclipse.org/api/organizations/').
		then(response => response.text()).
		then(text => {
			const organizations = JSON.parse(text);
			const content = [];
			if (organizations instanceof Array) {
				for (const organization of organizations) {
					const wgpas = organization.wgpas;
					if (wgpas instanceof Array) {
						for (const wgpa of wgpas) {
							if (wgpa.working_group == 'eclipse-ide') {
								const logo = organization.logos.web;
								content.push(`<span class="ide-wg-member-logo-container"><a href="eclipse+external:${organization.website}"><img class="ide-wg-member-logo" src="${logo}"/></a></span>`);
							}
						}
					}
				}
			}

			for (let i = content.length - 1; i > 0; i--) {
				const j = Math.floor(Math.random() * (i + 1));
				[content[i], content[j]] = [content[j], content[i]];
			}

			const sponsorURL = new URL("eclipse+external:https://www.eclipse.org/sponsor/ide");
			const location = window.location;
			if (location.href.startsWith(scriptBase)) {
				const url = new URL(location.href);
				url.search = '';
				url.hash = '';
				const relativeLocation = url.href.replace(scriptBase, '').replace(/\/$/, '');
				sponsorURL.searchParams.set('notification', relativeLocation);
			}
			for (const [k, v] of new URLSearchParams(location.search).entries()) {
				if (k != 'color' && k != 'background-color') {
					sponsorURL.searchParams.set(k, v);
				}
			}

			content.push(`
<div style="margin-top: .5em">				
	<label class="button">	
		<a id="sponsor-link" href="${sponsorURL}">${spanify('Become a Sponsor')}</a>
	</label>
</div>`);

			element.innerHTML += content.join('\n');
			element.style.display = "block";
		});
}

function spanify(string) {
	return string.split('').map(c => `<span>${c == ' ' ? '&nbsp;' : c}</span>`).join('');
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
