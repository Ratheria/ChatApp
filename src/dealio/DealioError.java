/**
 *	@author Ariana Fairbanks
 */

package dealio;

public enum DealioError
{	//Beyond this first row, the alignment here wasn't actually by design. It was spooky.
	unexpected_dealio_type("unexpected_dealio_type"), client_time_out("client_time_out"), 
	special_unsupported("special_unsupported"), file_size_exceeded("file_size_exceeded"),
	id_not_found("id_not_found"), user_name_length_exceeded("user_name_length_exceeded"), 
	malformed_dealio("malformed_dealio"), unsupported_file_type("unsupported_file_type"),
	message_exceeded_max_length("message_exceeded_max_length");
	
	public String text;
	
	private DealioError(String text)
	{
		this.text = text;
	}
}
