package org.springframework.samples.petclinic.system;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Global exception handler for the application. Routes errors to the appropriate branded
 * error page based on the request URI. Requests under {@code /patriot/**} or
 * {@code /businesses/**} are routed to the Patriot Thanks error template; all other
 * requests fall back to the default AthLeagues error template.
 *
 * @author Edward McKeown
 */
@ControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * Handles {@link NoResourceFoundException} (404 Not Found). Routes to the Patriot
	 * Thanks error template for patriot/business paths, otherwise falls back to the
	 * AthLeagues 404 template.
	 * @param ex the exception that was thrown
	 * @param request the current HTTP request (used to determine routing)
	 * @return the view name for the appropriate error template
	 */
	@ExceptionHandler(NoResourceFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public String handleNotFound(NoResourceFoundException ex, HttpServletRequest request) {
		if (isPatriotRequest(request)) {
			return "patriot/patriotError";
		}
		return "error/404";
	}

	/**
	 * Handles {@link ResponseStatusException} thrown from controllers (e.g. a 404 thrown
	 * when a business or incentive slug is not found). Routes to the Patriot Thanks error
	 * template for patriot/business paths.
	 * @param ex the exception that was thrown
	 * @param request the current HTTP request
	 * @param model the {@link Model} to populate with status and message
	 * @return the view name for the appropriate error template
	 */
	@ExceptionHandler(ResponseStatusException.class)
	public String handleResponseStatus(ResponseStatusException ex, HttpServletRequest request, Model model) {
		model.addAttribute("status", ex.getStatusCode().value());
		model.addAttribute("message", ex.getReason());
		if (isPatriotRequest(request)) {
			return "patriot/patriotError";
		}
		return "error";
	}

	/**
	 * Handles any unhandled {@link Exception} for Patriot Thanks paths. Renders the
	 * Patriot Thanks branded 500 error page so users never see the AthLeagues pet image
	 * after a server error on a patriot or business route.
	 * @param ex the unhandled exception
	 * @param request the current HTTP request
	 * @param model the {@link Model} to populate with the error status
	 * @return the view name for the appropriate error template
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleGenericError(Exception ex, HttpServletRequest request, Model model) {
		model.addAttribute("status", 500);
		model.addAttribute("message", ex.getMessage());
		if (isPatriotRequest(request)) {
			return "patriot/patriotError";
		}
		return "error";
	}

	/**
	 * Returns {@code true} if the request URI is under {@code /patriot/**} or
	 * {@code /businesses/**}, indicating that the Patriot Thanks branded error page
	 * should be used.
	 * @param request the current HTTP request
	 * @return {@code true} for Patriot Thanks routes, {@code false} otherwise
	 */
	private boolean isPatriotRequest(HttpServletRequest request) {
		String uri = request.getRequestURI();
		return uri != null && (uri.startsWith("/patriot") || uri.startsWith("/businesses"));
	}

}
