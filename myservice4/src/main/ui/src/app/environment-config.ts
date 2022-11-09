import { environment as env, environments } from '../environments/environment';

export function envConfig() {
	let apiDetails = { users:"", authorize: "", login: "", userProfile: "", audit: "", workflow: "", extern: "" };
	
	switch (location.hostname) {
		case 'localhost':
			env.baseUrl = '';
			break;
		case 'unistar-dev.com':
			env.baseUrl = environments.dev.baseUrl;
			break;
		case 'unistar-qa.com':
			env.baseUrl = environments.qat.baseUrl;
			break;
		case 'unistar-uat.com':
			env.baseUrl = environments.uat.baseUrl;
			break;
		case 'www.unistar.com':
			env.baseUrl = environments.production.baseUrl;
			break;
	}
	
	apiDetails = {
        users: 'v1',
		authorize: 'my-api/entitlements/v1/auth', 
		login: 'my-api/entitlements/v1/users/login', 
		userProfile: 'my-api/v1/userProfile', 
		audit: 'my-api/audit/v1/event', 
		workflow: 'my-api/v1/workflow',
		extern: 'my-api/broker/v1'
	};
	
	return apiDetails;
}
