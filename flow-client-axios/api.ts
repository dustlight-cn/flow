/* tslint:disable */
/* eslint-disable */
/**
 * 流程服务
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v1
 * Contact: hansin@goodvoice.com
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


import { Configuration } from './configuration';
import globalAxios, { AxiosPromise, AxiosInstance } from 'axios';
// Some imports not used depending on template conditions
// @ts-ignore
import { DUMMY_BASE_URL, assertParamExists, setApiKeyToObject, setBasicAuthToObject, setBearerAuthToObject, setOAuthToObject, setSearchParams, serializeDataIfNeeded, toPathString, createRequestFunction } from './common';
// @ts-ignore
import { BASE_PATH, COLLECTION_FORMATS, RequestArgs, BaseAPI, RequiredError } from './base';

/**
 * 
 * @export
 * @interface InstanceError
 */
export interface InstanceError {
    /**
     * 
     * @type {string}
     * @memberof InstanceError
     */
    message?: string;
    /**
     * 
     * @type {string}
     * @memberof InstanceError
     */
    type?: string;
}
/**
 * 
 * @export
 * @interface InstanceObject
 */
export interface InstanceObject {
    /**
     * 
     * @type {string}
     * @memberof InstanceObject
     */
    name?: string;
    /**
     * 
     * @type {number}
     * @memberof InstanceObject
     */
    version?: number;
    /**
     * 
     * @type {Array<object>}
     * @memberof InstanceObject
     */
    events?: Array<object>;
    /**
     * 
     * @type {string}
     * @memberof InstanceObject
     */
    clientId?: string;
    /**
     * 
     * @type {number}
     * @memberof InstanceObject
     */
    id?: number;
    /**
     * 
     * @type {string}
     * @memberof InstanceObject
     */
    elementType?: string;
    /**
     * 
     * @type {InstanceError}
     * @memberof InstanceObject
     */
    error?: InstanceError;
    /**
     * 
     * @type {string}
     * @memberof InstanceObject
     */
    status?: InstanceObjectStatusEnum;
    /**
     * 
     * @type {string}
     * @memberof InstanceObject
     */
    elementId?: string;
    /**
     * 
     * @type {string}
     * @memberof InstanceObject
     */
    updatedAt?: string;
    /**
     * 
     * @type {string}
     * @memberof InstanceObject
     */
    createdAt?: string;
}

/**
    * @export
    * @enum {string}
    */
export enum InstanceObjectStatusEnum {
    Active = 'ACTIVE',
    Canceled = 'CANCELED',
    Completed = 'COMPLETED',
    Incident = 'INCIDENT',
    Resolved = 'RESOLVED'
}

/**
 * 
 * @export
 * @interface ProcessObject
 */
export interface ProcessObject {
    /**
     * 
     * @type {string}
     * @memberof ProcessObject
     */
    name?: string;
    /**
     * 
     * @type {number}
     * @memberof ProcessObject
     */
    id?: number;
    /**
     * 
     * @type {string}
     * @memberof ProcessObject
     */
    owner?: string;
    /**
     * 
     * @type {number}
     * @memberof ProcessObject
     */
    version?: number;
    /**
     * 
     * @type {object}
     * @memberof ProcessObject
     */
    data?: object;
    /**
     * 
     * @type {string}
     * @memberof ProcessObject
     */
    clientId?: string;
    /**
     * 
     * @type {string}
     * @memberof ProcessObject
     */
    createdAt?: string;
}

/**
 * DefaultApi - axios parameter creator
 * @export
 */
export const DefaultApiAxiosParamCreator = function (configuration?: Configuration) {
    return {
        /**
         * 
         * @summary 通过 ID 取消运行实例
         * @param {number} id 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        cancelInstance: async (id: number, cid?: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            assertParamExists('cancelInstance', 'id', id)
            const localVarPath = `/v1/instance/{id}`
                .replace(`{${"id"}}`, encodeURIComponent(String(id)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'DELETE', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 创建流程实例
         * @param {string} name 
         * @param {{ [key: string]: object; }} requestBody 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createInstance: async (name: string, requestBody: { [key: string]: object; }, cid?: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('createInstance', 'name', name)
            // verify required parameter 'requestBody' is not null or undefined
            assertParamExists('createInstance', 'requestBody', requestBody)
            const localVarPath = `/v1/instance`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (name !== undefined) {
                localVarQueryParameter['name'] = name;
            }

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }


    
            localVarHeaderParameter['Content-Type'] = 'application/json';

            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(requestBody, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 发布消息
         * @param {string} name 
         * @param {string} key 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createMessage: async (name: string, key: string, cid?: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('createMessage', 'name', name)
            // verify required parameter 'key' is not null or undefined
            assertParamExists('createMessage', 'key', key)
            const localVarPath = `/v1/message`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (name !== undefined) {
                localVarQueryParameter['name'] = name;
            }

            if (key !== undefined) {
                localVarQueryParameter['key'] = key;
            }

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 创建流程
         * @param {string} body 
         * @param {string} [cid] 
         * @param {boolean} [base64] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createProcess: async (body: string, cid?: string, base64?: boolean, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'body' is not null or undefined
            assertParamExists('createProcess', 'body', body)
            const localVarPath = `/v1/process`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'POST', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }

            if (base64 !== undefined) {
                localVarQueryParameter['base64'] = base64;
            }


    
            localVarHeaderParameter['Content-Type'] = 'application/xml';

            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};
            localVarRequestOptions.data = serializeDataIfNeeded(body, localVarRequestOptions, configuration)

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 通过 ID 获取流程实例
         * @param {number} id 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getInstance: async (id: number, cid?: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'id' is not null or undefined
            assertParamExists('getInstance', 'id', id)
            const localVarPath = `/v1/instance/{id}`
                .replace(`{${"id"}}`, encodeURIComponent(String(id)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 查询流程实例
         * @param {string} [name] 
         * @param {number} [version] 
         * @param {Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>} [status] 
         * @param {number} [page] 
         * @param {number} [size] 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getInstances: async (name?: string, version?: number, status?: Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>, page?: number, size?: number, cid?: string, options: any = {}): Promise<RequestArgs> => {
            const localVarPath = `/v1/instances`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (name !== undefined) {
                localVarQueryParameter['name'] = name;
            }

            if (version !== undefined) {
                localVarQueryParameter['version'] = version;
            }

            if (status) {
                localVarQueryParameter['status'] = Array.from(status);
            }

            if (page !== undefined) {
                localVarQueryParameter['page'] = page;
            }

            if (size !== undefined) {
                localVarQueryParameter['size'] = size;
            }

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 通过名称获取流程
         * @param {string} name 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getProcess: async (name: string, cid?: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('getProcess', 'name', name)
            const localVarPath = `/v1/process/{name}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 通过名称与版本号获取流程
         * @param {string} name 
         * @param {number} version 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getProcess1: async (name: string, version: number, cid?: string, options: any = {}): Promise<RequestArgs> => {
            // verify required parameter 'name' is not null or undefined
            assertParamExists('getProcess1', 'name', name)
            // verify required parameter 'version' is not null or undefined
            assertParamExists('getProcess1', 'version', version)
            const localVarPath = `/v1/process/{name}/{version}`
                .replace(`{${"name"}}`, encodeURIComponent(String(name)))
                .replace(`{${"version"}}`, encodeURIComponent(String(version)));
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
        /**
         * 
         * @summary 获取流程列表
         * @param {string} [q] 
         * @param {string} [cid] 
         * @param {number} [page] 
         * @param {number} [size] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getProcesses: async (q?: string, cid?: string, page?: number, size?: number, options: any = {}): Promise<RequestArgs> => {
            const localVarPath = `/v1/processes`;
            // use dummy base URL string because the URL constructor only accepts absolute URLs.
            const localVarUrlObj = new URL(localVarPath, DUMMY_BASE_URL);
            let baseOptions;
            if (configuration) {
                baseOptions = configuration.baseOptions;
            }

            const localVarRequestOptions = { method: 'GET', ...baseOptions, ...options};
            const localVarHeaderParameter = {} as any;
            const localVarQueryParameter = {} as any;

            // authentication auth required
            // oauth required
            await setOAuthToObject(localVarHeaderParameter, "auth", [], configuration)

            if (q !== undefined) {
                localVarQueryParameter['q'] = q;
            }

            if (cid !== undefined) {
                localVarQueryParameter['cid'] = cid;
            }

            if (page !== undefined) {
                localVarQueryParameter['page'] = page;
            }

            if (size !== undefined) {
                localVarQueryParameter['size'] = size;
            }


    
            setSearchParams(localVarUrlObj, localVarQueryParameter, options.query);
            let headersFromBaseOptions = baseOptions && baseOptions.headers ? baseOptions.headers : {};
            localVarRequestOptions.headers = {...localVarHeaderParameter, ...headersFromBaseOptions, ...options.headers};

            return {
                url: toPathString(localVarUrlObj),
                options: localVarRequestOptions,
            };
        },
    }
};

/**
 * DefaultApi - functional programming interface
 * @export
 */
export const DefaultApiFp = function(configuration?: Configuration) {
    const localVarAxiosParamCreator = DefaultApiAxiosParamCreator(configuration)
    return {
        /**
         * 
         * @summary 通过 ID 取消运行实例
         * @param {number} id 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async cancelInstance(id: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.cancelInstance(id, cid, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary 创建流程实例
         * @param {string} name 
         * @param {{ [key: string]: object; }} requestBody 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createInstance(name: string, requestBody: { [key: string]: object; }, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<InstanceObject>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.createInstance(name, requestBody, cid, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary 发布消息
         * @param {string} name 
         * @param {string} key 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createMessage(name: string, key: string, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.createMessage(name, key, cid, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary 创建流程
         * @param {string} body 
         * @param {string} [cid] 
         * @param {boolean} [base64] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async createProcess(body: string, cid?: string, base64?: boolean, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.createProcess(body, cid, base64, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary 通过 ID 获取流程实例
         * @param {number} id 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getInstance(id: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<InstanceObject>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getInstance(id, cid, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary 查询流程实例
         * @param {string} [name] 
         * @param {number} [version] 
         * @param {Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>} [status] 
         * @param {number} [page] 
         * @param {number} [size] 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getInstances(name?: string, version?: number, status?: Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>, page?: number, size?: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<Array<InstanceObject>>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getInstances(name, version, status, page, size, cid, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary 通过名称获取流程
         * @param {string} name 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getProcess(name: string, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ProcessObject>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getProcess(name, cid, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary 通过名称与版本号获取流程
         * @param {string} name 
         * @param {number} version 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getProcess1(name: string, version: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ProcessObject>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getProcess1(name, version, cid, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
        /**
         * 
         * @summary 获取流程列表
         * @param {string} [q] 
         * @param {string} [cid] 
         * @param {number} [page] 
         * @param {number} [size] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        async getProcesses(q?: string, cid?: string, page?: number, size?: number, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<Array<ProcessObject>>> {
            const localVarAxiosArgs = await localVarAxiosParamCreator.getProcesses(q, cid, page, size, options);
            return createRequestFunction(localVarAxiosArgs, globalAxios, BASE_PATH, configuration);
        },
    }
};

/**
 * DefaultApi - factory interface
 * @export
 */
export const DefaultApiFactory = function (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) {
    const localVarFp = DefaultApiFp(configuration)
    return {
        /**
         * 
         * @summary 通过 ID 取消运行实例
         * @param {number} id 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        cancelInstance(id: number, cid?: string, options?: any): AxiosPromise<void> {
            return localVarFp.cancelInstance(id, cid, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 创建流程实例
         * @param {string} name 
         * @param {{ [key: string]: object; }} requestBody 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createInstance(name: string, requestBody: { [key: string]: object; }, cid?: string, options?: any): AxiosPromise<InstanceObject> {
            return localVarFp.createInstance(name, requestBody, cid, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 发布消息
         * @param {string} name 
         * @param {string} key 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createMessage(name: string, key: string, cid?: string, options?: any): AxiosPromise<void> {
            return localVarFp.createMessage(name, key, cid, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 创建流程
         * @param {string} body 
         * @param {string} [cid] 
         * @param {boolean} [base64] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        createProcess(body: string, cid?: string, base64?: boolean, options?: any): AxiosPromise<void> {
            return localVarFp.createProcess(body, cid, base64, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 通过 ID 获取流程实例
         * @param {number} id 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getInstance(id: number, cid?: string, options?: any): AxiosPromise<InstanceObject> {
            return localVarFp.getInstance(id, cid, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 查询流程实例
         * @param {string} [name] 
         * @param {number} [version] 
         * @param {Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>} [status] 
         * @param {number} [page] 
         * @param {number} [size] 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getInstances(name?: string, version?: number, status?: Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>, page?: number, size?: number, cid?: string, options?: any): AxiosPromise<Array<InstanceObject>> {
            return localVarFp.getInstances(name, version, status, page, size, cid, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 通过名称获取流程
         * @param {string} name 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getProcess(name: string, cid?: string, options?: any): AxiosPromise<ProcessObject> {
            return localVarFp.getProcess(name, cid, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 通过名称与版本号获取流程
         * @param {string} name 
         * @param {number} version 
         * @param {string} [cid] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getProcess1(name: string, version: number, cid?: string, options?: any): AxiosPromise<ProcessObject> {
            return localVarFp.getProcess1(name, version, cid, options).then((request) => request(axios, basePath));
        },
        /**
         * 
         * @summary 获取流程列表
         * @param {string} [q] 
         * @param {string} [cid] 
         * @param {number} [page] 
         * @param {number} [size] 
         * @param {*} [options] Override http request option.
         * @throws {RequiredError}
         */
        getProcesses(q?: string, cid?: string, page?: number, size?: number, options?: any): AxiosPromise<Array<ProcessObject>> {
            return localVarFp.getProcesses(q, cid, page, size, options).then((request) => request(axios, basePath));
        },
    };
};

/**
 * DefaultApi - object-oriented interface
 * @export
 * @class DefaultApi
 * @extends {BaseAPI}
 */
export class DefaultApi extends BaseAPI {
    /**
     * 
     * @summary 通过 ID 取消运行实例
     * @param {number} id 
     * @param {string} [cid] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public cancelInstance(id: number, cid?: string, options?: any) {
        return DefaultApiFp(this.configuration).cancelInstance(id, cid, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 创建流程实例
     * @param {string} name 
     * @param {{ [key: string]: object; }} requestBody 
     * @param {string} [cid] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public createInstance(name: string, requestBody: { [key: string]: object; }, cid?: string, options?: any) {
        return DefaultApiFp(this.configuration).createInstance(name, requestBody, cid, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 发布消息
     * @param {string} name 
     * @param {string} key 
     * @param {string} [cid] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public createMessage(name: string, key: string, cid?: string, options?: any) {
        return DefaultApiFp(this.configuration).createMessage(name, key, cid, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 创建流程
     * @param {string} body 
     * @param {string} [cid] 
     * @param {boolean} [base64] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public createProcess(body: string, cid?: string, base64?: boolean, options?: any) {
        return DefaultApiFp(this.configuration).createProcess(body, cid, base64, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 通过 ID 获取流程实例
     * @param {number} id 
     * @param {string} [cid] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public getInstance(id: number, cid?: string, options?: any) {
        return DefaultApiFp(this.configuration).getInstance(id, cid, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 查询流程实例
     * @param {string} [name] 
     * @param {number} [version] 
     * @param {Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>} [status] 
     * @param {number} [page] 
     * @param {number} [size] 
     * @param {string} [cid] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public getInstances(name?: string, version?: number, status?: Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>, page?: number, size?: number, cid?: string, options?: any) {
        return DefaultApiFp(this.configuration).getInstances(name, version, status, page, size, cid, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 通过名称获取流程
     * @param {string} name 
     * @param {string} [cid] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public getProcess(name: string, cid?: string, options?: any) {
        return DefaultApiFp(this.configuration).getProcess(name, cid, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 通过名称与版本号获取流程
     * @param {string} name 
     * @param {number} version 
     * @param {string} [cid] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public getProcess1(name: string, version: number, cid?: string, options?: any) {
        return DefaultApiFp(this.configuration).getProcess1(name, version, cid, options).then((request) => request(this.axios, this.basePath));
    }

    /**
     * 
     * @summary 获取流程列表
     * @param {string} [q] 
     * @param {string} [cid] 
     * @param {number} [page] 
     * @param {number} [size] 
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof DefaultApi
     */
    public getProcesses(q?: string, cid?: string, page?: number, size?: number, options?: any) {
        return DefaultApiFp(this.configuration).getProcesses(q, cid, page, size, options).then((request) => request(this.axios, this.basePath));
    }
}


