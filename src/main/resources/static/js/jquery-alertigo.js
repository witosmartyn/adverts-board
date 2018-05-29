/*
	* Alertigo jQuery Plugin
	*
	* @file: jquery-alertigo.js
	* @author: Mark Coyne
	* @site: www.coynem.com
	* @license: MIT License
*/

function alertigo(text, options) {
	// default settings
	var settings = {
		'development': false,
		'life': '3000',
		'sticky': false
	};
	
	// extend settings
	options = typeof options == 'object' ? $.extend(settings, options) : settings;
	color = options.color ? 'alertigo-' + options.color : '';


	// create content
	var content = $('<div class="alertigo ' + color + '">' + text + '</div>').fadeIn();



	// append content to div
	$('div#alertigo').append(content);
	
	
	
	
	// close alertigo after designated time
	if (!options.sticky) {
		setTimeout(function() {
			alertigo_close(content);
		}, options.life);
	};



	// on click - element
	content.on('click', function() {
		alertigo_close(content);
	});
};



/*
	* alertigo_close
	* close alert - could add in more options
*/
function alertigo_close(element) {
	element.fadeOut('normal', function() {
		element.remove();
	});
}



/*
	* debug function
	* send console i18n if development setting is turned on
*/
function debug(debug, message) {
	if (debug && window.console && window.console.log) {
		window.console.log('jQuery-Alertigo: ' + message);
	};
}
