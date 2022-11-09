import { envConfig } from '../../environment-config';
import { environment } from 'src/environments/environment';

export class AppConstants {
	public static readonly URLS = envConfig();
	
	public static readonly GET_LOGIN_DETAILS = AppConstants.URLS['login'];
}
