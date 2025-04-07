var eclipse_org_common = { "settings": { "cookies_class": { "name": "eclipse_settings", "enabled": 1 } } };

window.onscroll = function() {
	const footer = document.querySelector("#footer>div>a");
	footer.style.display = document.documentElement.scrollTop > 100 ? 'inline' : 'none';
};

const bannerLogo = `https://eclipse.dev/eclipse.org-common/themes/solstice/public/images/logo/eclipse-ide/eclipse_logo.svg`;

const logo = 'https://projects.eclipse.org/modules/custom/eclipsefdn/eclipsefdn_projects/images/logos/default.png';

const projectBase = new URL(".", document.currentScript.src).href;

const home = `${projectBase}index.html`;

const projectEclipseOrg = "https://projects.eclipse.org/projects/";

const apiProjects = 'https://projects.eclipse.org/api/projects/';

const apiGitHubSimRel = 'https://api.github.com/repos/eclipse-simrel/simrel.build/contents/';

const genericProductId = 'generic';

const imagesPrefix = 'https://www.eclipse.org/downloads/images/';

const productImages = new Map([
	['committers', `${imagesPrefix}committers.png`],
	['cpp', `${imagesPrefix}cdt.png`],
	['dsl', `${imagesPrefix}dsl-package_42.png`],
	['embedcpp', `${imagesPrefix}cdt.png`],
	['java', `${imagesPrefix}java.png`],
	['jee', `${imagesPrefix}javaee.png`],
	['modeling', `${imagesPrefix}modeling.png`],
	['php', `${imagesPrefix}php.png`],
	['rcp', `${imagesPrefix}rcp.png`],
	['scout', `${imagesPrefix}scout.jpg`],
	['sdk', `${imagesPrefix}committers.png`],
	[genericProductId, `${imagesPrefix}committers.png`],
]);

function getProductImage(productId) {
	return productImages.get(productId) ?? productImages.get(genericProductId);
}

const productPrimaryProjects = new Map([
	['committers', ['eclipse.jdt', 'eclipse.pde']],
	['cpp', ['tools.cdt', 'tools.linuxtools', 'tools.tracecompass']],
	['dsl', ['modeling.tmf.xtext', 'eclipse.jdt']],
	['embedcpp', ['iot.embed-cdt', 'tools.linuxtools', 'tools.tracecompass']],
	['java', ['eclipse.jdt', 'technology.m2e']],
	['jee', ['eclipse.jdt', 'webtools.servertools', 'webtools.webservices', 'webtools.sourceediting', 'webtools.jsdt', 'webtools.jeetools', 'webtools.jsf', 'webtools.dali', 'technology.m2e', 'eclipse.platform', 'tools.datatools', 'webtools.common']],
	['modeling', ['modeling.sirius', 'modeling.ecoretools', 'modeling.emf.emf', 'modeling.emfservices', 'modeling.gmf-runtime']],
	['php', ['tools.pdt']],
	['rcp', ['rt.rap', 'eclipse.jdt', 'eclipse.platform']],
	['scout', ['technology.scout', 'eclipse.jdt']],
	['sdk', []],
	[genericProductId, []],
]);

const productNames = new Map([
	['committers', 'Eclipse IDE for Eclipse Committers'],
	['cpp', 'Eclipse IDE for C/C++ Developers'],
	['dsl', 'Eclipse IDE for Java and DSL Developers'],
	['embedcpp', 'Eclipse IDE for Embedded C/C++ Developers'],
	['java', 'Eclipse IDE for Java Developers'],
	['jee', 'Eclipse IDE for Enterprise Java and Web Developers'],
	['modeling', 'Eclipse Modeling Tools'],
	['php', 'Eclipse IDE for PHP Developers'],
	['rcp', 'Eclipse IDE for RCP and RAP Developers'],
	['scout', 'Eclipse IDE for Scout Developers'],
	['sdk', 'Eclipse SDK'],
	[genericProductId, 'Eclipse IDE'],
]);


const meta = toElements(`
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="shortcut icon" href="https://eclipseide.org/favicon.ico"/>
`);

const defaultHeader = toElements(`
	<a href="https://www.eclipse.org/downloads/packages/">Eclipse IDE</a>
	<a href="https://eclipseide.org/working-group/">Working Group</a>
	<a href="https://marketplace.eclipse.org/">Marketplace</a>
`);

const defaultBreadcrumb = toElements(`
	<a href="https://eclipseide.org/">Home</a>
	<a href="https://eclipseide.org/projects/">Projects</a>
`);

const defaultNav = toElements(`
<a class="fa-download" target="_out" href="https://www.eclipse.org/downloads/packages/"
	title="Downloads: Update Sites">
	Downloads<p>Update Site</p>
</a>
<a class="fa-map-o" target="_out" href="${projectEclipseOrg}"
	title="Projects: Portal">
	Projects<p>Portal</p>
</a>
<a class="fa-book" target="_out" href="https://help.eclipse.org/" title="Documentation: help.eclipse.org">
	Documentation<p>help.eclipse.org</p>
</a>
<a class="fa-github" target="_out" href="https://github.com/eclipse-ide"
	title="GitHub: Eclipse IDE">
	GitHub<p>Eclipse IDE</p>
</a>
`);

const sideBarProjectId = 'sidebar-project';

const defaultAside = toElements(`
<span class="separator"><i class='fa fa-cube'></i> Projects</span>
<a id="${sideBarProjectId}" href=""></a>
`);

function generate() {
	try {
		const head = document.head;
		var referenceNode = head.querySelector('script');
		for (const element of [...meta]) {
			head.insertBefore(element, referenceNode.nextElementSibling)
			referenceNode = element;
		}

		applyGenerators(document.querySelectorAll('[data-generate]'), document.querySelector('main'));

		const generatedBody = generateBody();
		document.body.replaceChildren(...generatedBody);

		applyGenerators(document.body.querySelectorAll('[data-generate]'));

		document.getElementById('nav-product-logo').src = getProductImage(getProduct());
	} catch (exception) {
		document.body.prepend(...toElements(`<span>Failed to generate content: <span><b style="color: FireBrick">${exception.message}</b><br/>`));
		console.log(exception);
	}
}

function applyGenerators(generators, exclude) {
	for (const element of generators) {
		if (exclude == null || !exclude.contains(element)) {
			const generator = element.getAttribute('data-generate');
			const generate = new Function(generator);
			generate.call(element, element);
		}
	}
}

function generateDefaults(element) {
	const parts = [];
	if (!hasElement('header')) {
		parts.push(generateDefaultHeader(document.createElement('div')));
	}
	if (!hasElement('breadcrumb')) {
		parts.push(generateDefaultBreadcrumb(document.createElement('div')));
	}
	if (!hasElement('aside')) {
		parts.push(generateDefaultAside(document.createElement('div')));
	}
	if (!hasElement('nav')) {
		parts.push(generateDefaultNav(document.createElement('div')));
	}
	element.prepend(...parts);
}

function generateBody() {
	const col = document.getElementById('aside') ? 'col-md-18' : ' col-md-24';
	return toElements(`
<div>
	${generateHeader()}
	<main id="content">
		<div class="novaContent container" id="novaContent">
			<div class="row">
				<div class="${col} main-col-content">
					<div class="novaContent" id="novaContent">
						<div class="row">
							${generateBreadcrumb()}
						</div>
						<div class=" main-col-content">
							${generateNav()}
							<div id="midcolumn">
							${generateMainContent()}
							</div>
						</div>
					</div>
				</div>
				${generateAside()}
			</div>
		</div>
	</main>
	<footer id="footer">
		<div class="container">
			<div class="footer-sections row equal-height-md font-bold">
				<div id="footer-eclipse-foundation" class="footer-section col-md-5 col-sm-8">
					<div class="menu-heading">Eclipse Foundation</div>
					<ul class="nav">
						<ul class="nav">
							<li><a href="http://www.eclipse.org/org/">About</a></li>
							<li><a href="https://projects.eclipse.org/">Projects</a></li>
							<li><a href="http://www.eclipse.org/collaborations/">Collaborations</a></li>
							<li><a href="http://www.eclipse.org/membership/">Membership</a></li>
							<li><a href="http://www.eclipse.org/sponsor/">Sponsor</a></li>
						</ul>
					</ul>
				</div>
				<div id="footer-legal" class="footer-section col-md-5 col-sm-8">
					<div class="menu-heading">Legal</div>
					<ul class="nav">
						<ul class="nav">
							<li><a href="http://www.eclipse.org/legal/privacy.php">Privacy Policy</a></li>
							<li><a href="http://www.eclipse.org/legal/termsofuse.php">Terms of Use</a></li>
							<li><a href="http://www.eclipse.org/legal/compliance/">Compliance</a></li>
							<li><a href="http://www.eclipse.org/org/documents/Community_Code_of_Conduct.php">Code of
									Conduct</a></li>
							<li><a href="http://www.eclipse.org/legal/">Legal Resources</a></li>
						</ul>
					</ul>
				</div>
				<div id="footer-more" class="footer-section col-md-5 col-sm-8">
					<div class="menu-heading">More</div>
					<ul class="nav">
						<ul class="nav">
							<li><a href="http://www.eclipse.org/security/">Report a Vulnerability</a></li>
							<li><a href="https://www.eclipsestatus.io/">Service Status</a></li>
							<li><a href="http://www.eclipse.org/org/foundation/contact.php">Contact</a></li>
							<li><a href="http://www.eclipse.org//projects/support/">Support</a></li>
						</ul>
					</ul>
				</div>
			</div>
			<div class="col-sm-24">
				<div class="row">
					<div id="copyright" class="col-md-16">
						<p id="copyright-text">Copyright © Eclipse Foundation AISBL. All Rights Reserved.</p>
					</div>
				</div>
			</div>
			<a href="#" class="scrollup" onclick="scrollToTop()">Back to the top</a>
		</div>
	</footer>
</div>
`);
}

function generateMainContent() {
	const main = document.body.querySelector('main')
	if (main != null) {
		return main.innerHTML;
	}
	return `
<main>The body specifies no content.</main>
`;
}

function generateDefaultHeader(element) {
	return prependChildren(element, 'header', ...defaultHeader);
}

function generateHeader() {
	const elements = document.querySelectorAll('#header>a');
	const items = Array.from(elements).map(link => {
		link.classList.add('link-unstyled');
		return `
<li class="navbar-nav-links-item">
	${link.outerHTML}
</li>
`;
	});
	const mobileItems = Array.from(elements).map(link => {
		link.className = 'mobile-menu-item mobile-menu-dropdown-toggle';
		return `
<li class="mobile-menu-dropdown">
	${link.outerHTML}
</li>
`;
	});

	return `
<header class="header-wrapper" id="header">
	<div class="header-navbar-wrapper">
		<div class="container">
			<div class="header-navbar">
				<a class="header-navbar-brand" href="https://eclipseide.org/">
					<div class="logo-wrapper">
						<img src="${bannerLogo}" alt="Eclipse IDE" width="150"/>
					</div>
				</a>
				<nav class="header-navbar-nav">
					<ul class="header-navbar-nav-links">
						${items.join('\n')}
					</ul>
				</nav>
				<div class="header-navbar-end">
					<div class="float-right hidden-xs" id="btn-call-for-action">
						<a target="_out" href="https://www.eclipse.org/sponsor/ide/" class="btn btn-huge btn-warning">
							<i class="fa fa-star"></i> Sponsor
						</a>
					</div>
					<button class="mobile-menu-btn" onclick="toggleMenu()">
						<i class="fa fa-bars fa-xl"/></i>
					</button>
				</div>
			</div>
		</div>
	</div>
	<nav id="mobile-menu" class="mobile-menu hidden" aria-expanded="false">
		<ul>
			${mobileItems.join('\n')}
		</ul>
	</nav>
</header>
`;
}

function generateDefaultBreadcrumb(element) {
	return prependChildren(element, 'breadcrumb', ...defaultBreadcrumb);
}

function generateBreadcrumb() {
	const breadcumbs = document.getElementById('breadcrumb')
	if (breadcumbs == null) {
		return '';
	}

	const elements = breadcumbs.children;
	const items = Array.from(elements).map(link => `<li>${link.outerHTML}</li>`);

	return `
<section class="default-breadcrumbs hidden-print breadcrumbs-default-margin"
	id="breadcrumb">
	<div class="container">
		<h3 class="sr-only">Breadcrumbs</h3>
		<div class="row">
			<div class="col-sm-24">
				<ol class="breadcrumb">
					${items.join('\n')}
				</ol>
			</div>
		</div>
	</div>
</section>
`;
}

function generateDefaultNav(element) {
	return prependChildren(element, 'nav', ...defaultNav);
}

function generateNav() {
	const elements = document.body.querySelectorAll('#nav>a');
	if (elements.length == 0) {
		return '';
	}

	const items = Array.from(elements).map(element => {
		const href = element.getAttribute('href')
		const target = element.getAttribute('target') ?? "_self";
		const title = element.getAttribute('title') ?? '';
		const className = element.className ?? '';
		const content = element.innerHTML;
		return `
<li class="col-xs-24 col-md-12">
	<a class="row" href="${href}" title="${title}"
		target="${target}">
		<i class="col-xs-3 col-md-6 fa ${className}"></i>
		<span class="col-xs-21 c col-md-17">${content}
		</span>
	</a>
</li>
`;
	});

	return `
<div class="header_nav">
	<div class="col-xs-24 col-md-10 vcenter">
		<a href="${home}">
			<img id='nav-product-logo' src="${logo}" alt="Eclipse IDE" xwidth="50%" xheight="auto" class="img-responsive header_nav_logo"/>
		</a>
	</div><!-- NO SPACES
 --><div class="col-xs-24 col-md-14 vcenter">
		<ul class="clearfix">
			${items.join('\n')}
		</ul>
	</div>
</div>
`;
}

function generateDefaultAside(element) {
	prependChildren(element, 'aside', ...defaultAside);

	if (getQueryParameter('test-products') == 'true') {
		const elements = [];
		elements.push(...toElements(`
<span class="separator"><i class='fa fa-cube'></i> Products</span>
		`));
		for (const [productId, productName] of productNames.entries()) {
			const fullProductId = genericProductId == productId ? genericProductId : 'sdk' == productId ? 'org.eclipse.sdk.ide' : `org.eclipse.epp.package.${productId}.product`;
			const img = `<img style="max-width: 1em; max-height:1em; padding:0; background: lightGray;" src="${getProductImage(productId)}"/>`;
			const shortProductName = productName.replace('Eclipse IDE for ', '');
			elements.push(...toElements(`
<a style="justify-content: left;" href="index.html?test-products=true&product-id=${fullProductId}">${img}&nbsp;&nbsp;${shortProductName}</a>`));
		}
		element.prepend(...elements);
	}
	return element;
}

function generateAside() {
	const elements = document.body.querySelectorAll('aside>*,#aside>*');
	if (elements.length == 0) {
		return '';
	}

	const items = Array.from(elements).map(element => {
		const main = element.classList.contains('separator')
		const id = element.id;
		const idAttribute = id == null ? '' : `id="$[id}"`;
		element.classList.add('link-unstyled');
		if (main) {
			element.classList.add('main-sidebar-heading');
			return `
<li ${idAttribute} class="main-sidebar-main-item main-sidebar-item-indented separator">
	${element.outerHTML}
</li>
`
		} else {
			return `
<li ${idAttribute} class="main-sidebar-item main-sidebar-item-indented">
	${element.outerHTML}
</li>
`
		}
	});

	return `
<div class="col-md-6 main-col-sidebar-nav">
	<aside class="main-sidebar-default-margin" id="main-sidebar">
		<ul class="ul-left-nav" id="leftnav" role="tablist" aria-multiselectable="true">
			${items.join('\n')}
	</aside>
</div>
`;
}

function generateTableOfContents(target) {
	if (target instanceof Element) {
		const headers = document.querySelectorAll('h1, h2, h3, h4');

		for (const header of headers) {
			if (header.id != '') {
				const count = Number(header.nodeName.substring(1));
				const fontSize = 100 + 10 * (4 - count);
				target.append(...toElements(`
<div><span style="font-size: ${fontSize}%; padding-left: ${count}em;">&#8226;&nbsp;<a  href="#${header.id}">${header.innerHTML}</a><span></div>
`))
			}
		}
	}
}


function sendRequest(location, handler) {
	var request = new XMLHttpRequest();
	request.open('GET', location);
	request.onloadend = function() {
		handler(request);
	};
	request.send();
}

function getText(location, handler) {
	sendRequest(location, request => {
		const json = JSON.parse(request.responseText);
		const binary = window.atob(json.content);
		const bytes = new Uint8Array(binary.length);
		for (var i = 0; i < binary.length; i++) {
			bytes[i] = binary.charCodeAt(i);
		}
		const decoder = new TextDecoder();
		const realText = decoder.decode(bytes);
		handler(realText);
	})
}

function getXML(location, handler) {
	getText(location, realText => {
		const parser = new DOMParser();
		const xmlDoc = parser.parseFromString(realText, "text/xml");
		handler(xmlDoc);
	})
}

function getSimRelAggran(handler) {
	getXML(`${apiGitHubSimRel}simrel.aggran`, handler);
}

function getProduct() {
	const productId = getQueryParameter('product-id');
	if (productId != null) {
		if (productId == 'org.eclipse.sdk.ide') {
			return 'sdk';
		}
		const simpleId = productId.replace(/org.eclipse.epp.package.([^.]+).product/, '$1');
		if (productNames.has(simpleId)) {
			return simpleId;
		}
	}
	return genericProductId;
}

function setProductName(element) {
	element.innerText = productNames.get(getProduct()) ?? productNames.get(genericProductId);
}

function getContribution(project) {
	return project.closest('contribution');
}

function isExcluded(project) {
	if (project.querySelectorAll(':scope > repository').length == 0) {
		return true;
	}
	const contribution = getContribution(project);
	return contribution == null || 'false' == contribution.getAttribute('enabled');
}

function getAllTags(project) {
	const allTags = getTags(project);
	const tags = [];
	for (const tag of productNames.keys()) {
		if (allTags.includes(tag)) {
			tags.push(tag);
		}
	}
	for (const tag of allTags) {
		if (!tags.includes(tag)) {
			tags.push(tag);
		}
	}
	return tags;
}

function getTags(project) {
	const tags = [];
	if (project instanceof Element) {
		for (const element of project.children) {
			if ('tag' == element.localName) {
				tags.push(element.textContent);
			}
		}
		if (project.localName != 'contribution') {
			tags.push(...getTags(project.parentElement));
		}
	}
	return tags;
}

const projectTags = new Map();
const projectRanks = new Map();

function getRank(project, projectId, productId) {
	const primaryProjects = productPrimaryProjects.get(productId) ?? productPrimaryProjects.get(genericProductId);
	const index = primaryProjects.indexOf(projectId);
	if (index != -1) {
		return 100 - index;
	}

	let rank = Number(project.getAttribute('rank') ?? '0');
	if (projectId == 'tools.oomph') {
		rank = 0;
	}
	return rank;
}

const organizationRepositories = new Map();

function getSimRelProjectIds(handler) {
	getSimRelAggran(xmlDoc => {
		const projects = xmlDoc.querySelectorAll('project,subproject');
		const projectIds = new Map();
		const productId = getProduct();
		for (const project of projects) {
			if (!isExcluded(project)) {
				const site = project.getAttribute('site')
				if (site.startsWith(projectEclipseOrg)) {
					const projectId = site.replace(projectEclipseOrg, '');
					projectIds.set(project.getAttribute('name'), projectId);
					let rank = getRank(project, projectId, productId);
					projectRanks.set(projectId, rank);
					if (!projectTags.has(projectId)) {
						projectTags.set(projectId, getAllTags(project));
					}
				}
			}
		}

		const entries = [...projectIds].sort((a, b) => {
			return a[0] > b[0]
		});

		const repositories = xmlDoc.querySelectorAll('repository');
		for (const repository of repositories) {
			const uri = repository.getAttribute('uri')
			const org = new URL('.', uri).href;
			if (!organizationRepositories.has(org)) {
				organizationRepositories.set(org, []);
			}
			const orgRepositories = organizationRepositories.get(org);
			const repoName = uri.replace(org, '');
			if (!orgRepositories.includes(repoName))
				orgRepositories.push(repoName);
		}

		handler(entries)
	});
}

function getProject(projectId, handler) {
	sendRequest(apiProjects + projectId, request => {
		const project = JSON.parse(request.responseText);
		handler(project[0]);
	});
}

function generateProjectDetails(target) {
	const productTag = getProduct();
	getSimRelProjectIds(projectIds => {
		const sideBarProjects = [];
		const elements = [];
		for (const [projectName, projectId] of projectIds) {
			const tags = projectTags.get(projectId);
			if (tags.includes(productTag) || productTag == genericProductId && tags.includes('sdk')) {
				sideBarProjects.push(projectId);
			}
			elements.push(...toElements(`
<hr/>
<div id="${projectId}">
Loading ${projectName} ${projectId}
</div>		
`));
			generateProjectDetail(projectId);
		}

		elements.push(...toElements('<hr/>'))
		target.replaceChildren(...elements);

		const entries = [...sideBarProjects].sort((a, b) => {
			return projectRanks.get(a) < projectRanks.get(b);
		});

		const sideBarItemPlaceholder = document.getElementById(sideBarProjectId);
		const sideBarItems = [];
		for (const projectId of entries) {
			const sideBarItem = sideBarItemPlaceholder.cloneNode(true)
			sideBarItem.id = `sidebar-${projectId}`;
			sideBarItem.innerHTML = '&nbsp;'
			sideBarItem.href = `#${projectId}`;
			sideBarItems.push(sideBarItem);
		}
		sideBarItemPlaceholder.replaceWith(...sideBarItems);
	});
}

function generateProjectDetail(projectId) {
	getProject(projectId, project => {
		const target = document.getElementById(projectId);
		var repo = 'missing';
		var type = 'fa-github'
		const gitHubOrg = project.github.org
		if (gitHubOrg) {
			repo = `https://github.com/${gitHubOrg}/`;
		} else {
			const gitHubRepos = project.github_repos;
			if (gitHubRepos.length > 0) {
				repo = gitHubRepos[0].url;
			} else {
				const gitLabOrg = project.gitlab.project_group
				if (gitLabOrg) {
					type = 'fa-gitlab';
					repo = `https://gitlab.eclipse.org/${gitLabOrg}/`;
				}
			}
		}

		const projectShortName = project.name.replace(/^Eclipse /, '');
		const links = document.getElementById('links');
		if (links != null) {
			// <a href="${projectBase}projectPages/callgraph/index.html">Callgraph</a>
			links.appendChild(...toElements(`<span>${projectId} <a href="#">${projectShortName}</a><br/>\n</span>`));
		}

		const sideBarItem = document.getElementById(`sidebar-${projectId}`)
		if (sideBarItem != null) {
			sideBarItem.textContent = projectShortName;
		}

		const tags = projectTags.get(projectId).map(tag => {
			return `<img class="product-logo" title="${productNames.get(tag) ?? productNames.get(genericProductId)}" src="${getProductImage(tag)}"/>`;
		}).join('');

		const repos = organizationRepositories.get(repo) ?? [];
		const orgRepos = repos.map(orgRepo => {
			return `<a target="_out" href="${repo + orgRepo}"><i class="fa fa-git-square"></i>&nbsp;${orgRepo}</a>`;
		});


		const item = `
<table><tbody><tr>
<td style="vertical-align: top;">
<a target="_out" href="${projectEclipseOrg}${projectId}"><img class="logo" src="${project.logo}" alt="${projectId}"/></a>
</td>
<td>
<h4>${projectShortName}<a style="text-decoration: none" target="_out" href="${project.website_url.replace('http:', 'https:')}"> <span style="font-size: 66%">&#x1F517;</span></a>${tags}</h4>
${project.summary}
<div style="margin-top: .5em">
<a target="_out" href="${repo}"><i class="fa ${type}"></i> ${repo}</a>
${orgRepos.join('\n')}
</div>
</td>
</tr></tbody></table>
`;
		target.innerHTML = item;
	});
}

function generateQueryParametersTable(target) {
	const search = new URLSearchParams(window.location.search);
	if (search.size > 0) {
		const items = Array.from(search.entries()).map(entry => {
			const tr = document.createElement('tr')
			const propertyTD = document.createElement('td')
			propertyTD.innerText = entry[0];
			const valueTD = document.createElement('td')
			valueTD.innerText = entry[1];
			tr.appendChild(propertyTD);
			tr.appendChild(valueTD);
			return tr.outerHTML
		});

		const elements = toElements(`
<table>
	<tbody>
		<tr>
			<th>Property</th>
			<th>Value</th>
		</tr>
		${items.join('\n')}
	</tbody>
</table>
`);
		replaceChildren(target, 'query-parameter-table', ...elements);
	}
}

function hasElement(id) {
	return document.getElementById(id) != null;
}

function toElements(text) {
	const wrapper = document.createElement('div');
	wrapper.innerHTML = text;
	return wrapper.children
}

function replaceChildren(element, id, ...children) {
	element.id = id;
	element.replaceChildren(...children);
	return element;
}

function prependChildren(element, id, ...children) {
	element.id = id;
	element.prepend(...children);
	return element;
}

function addBase(htmlDocument, location) {
	const base = htmlDocument.createElement('base');
	base.href = location;
	htmlDocument.head.appendChild(base)
}

function getQueryParameter(id) {
	const location = new URL(window.location);
	const search = new URLSearchParams(location.search);
	return search.get(id);
}

function toggleMenu() {
	const mobileMenu = document.getElementById('mobile-menu')
	if (mobileMenu.classList.contains('hidden')) {
		mobileMenu.classList.remove('hidden');
	} else {
		mobileMenu.classList.add('hidden');
	}
}

function scrollToTop() {
	window.scrollTo({ top: 0, behavior: 'smooth' });
}
