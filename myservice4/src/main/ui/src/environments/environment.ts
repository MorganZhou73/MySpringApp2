// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  baseUrl: '' // empty means the config in proxy/config.json
};

export const environments = {
	dev: {    
		baseUrl: 'https://unistar-dev.com/',
	},
	qat: {
		baseUrl: 'https://unistar-qa.com/',	
	},
	uat: {
		baseUrl: 'https://unistar-uat.com/',	
	},
	production: {
		baseUrl: 'https://www.unistar.com/',	
	}
};
