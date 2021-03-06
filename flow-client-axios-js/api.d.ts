/**
 * 流程服务
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v1
 * Contact: hansin@dustlight.cn
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
import { Configuration } from './configuration';
import { AxiosPromise, AxiosInstance } from 'axios';
import { RequestArgs, BaseAPI } from './base';
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
     * @type {string}
     * @memberof InstanceObject
     */
    createdAt?: string;
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
    updatedAt?: string;
    /**
     *
     * @type {string}
     * @memberof InstanceObject
     */
    elementId?: string;
    /**
     *
     * @type {InstanceError}
     * @memberof InstanceObject
     */
    error?: InstanceError;
}
/**
    * @export
    * @enum {string}
    */
export declare enum InstanceObjectStatusEnum {
    Active = "ACTIVE",
    Canceled = "CANCELED",
    Completed = "COMPLETED",
    Incident = "INCIDENT",
    Resolved = "RESOLVED"
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
 *
 * @export
 * @interface QueryResultInstanceObject
 */
export interface QueryResultInstanceObject {
    /**
     *
     * @type {number}
     * @memberof QueryResultInstanceObject
     */
    count?: number;
    /**
     *
     * @type {Array<InstanceObject>}
     * @memberof QueryResultInstanceObject
     */
    data?: Array<InstanceObject>;
}
/**
 *
 * @export
 * @interface QueryResultProcessObject
 */
export interface QueryResultProcessObject {
    /**
     *
     * @type {number}
     * @memberof QueryResultProcessObject
     */
    count?: number;
    /**
     *
     * @type {Array<ProcessObject>}
     * @memberof QueryResultProcessObject
     */
    data?: Array<ProcessObject>;
}
/**
 *
 * @export
 * @interface QueryResultUserTask
 */
export interface QueryResultUserTask {
    /**
     *
     * @type {number}
     * @memberof QueryResultUserTask
     */
    count?: number;
    /**
     *
     * @type {Array<UserTask>}
     * @memberof QueryResultUserTask
     */
    data?: Array<UserTask>;
}
/**
 *
 * @export
 * @interface UserTask
 */
export interface UserTask {
    /**
     *
     * @type {number}
     * @memberof UserTask
     */
    id?: number;
    /**
     *
     * @type {UserTaskTarget}
     * @memberof UserTask
     */
    target?: UserTaskTarget;
    /**
     *
     * @type {string}
     * @memberof UserTask
     */
    clientId?: string;
    /**
     *
     * @type {string}
     * @memberof UserTask
     */
    completedAt?: string;
    /**
     *
     * @type {string}
     * @memberof UserTask
     */
    form?: string;
    /**
     *
     * @type {{ [key: string]: object; }}
     * @memberof UserTask
     */
    variables?: {
        [key: string]: object;
    };
    /**
     *
     * @type {string}
     * @memberof UserTask
     */
    user?: string;
    /**
     *
     * @type {string}
     * @memberof UserTask
     */
    elementId?: string;
    /**
     *
     * @type {number}
     * @memberof UserTask
     */
    instanceId?: number;
    /**
     *
     * @type {string}
     * @memberof UserTask
     */
    processName?: string;
    /**
     *
     * @type {number}
     * @memberof UserTask
     */
    processId?: number;
}
/**
 *
 * @export
 * @interface UserTaskTarget
 */
export interface UserTaskTarget {
    /**
     *
     * @type {Array<string>}
     * @memberof UserTaskTarget
     */
    roles?: Array<string>;
    /**
     *
     * @type {Array<string>}
     * @memberof UserTaskTarget
     */
    users?: Array<string>;
}
/**
 * InstancesApi - axios parameter creator
 * @export
 */
export declare const InstancesApiAxiosParamCreator: (configuration?: Configuration) => {
    /**
     *
     * @summary 通过 ID 取消运行实例
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    cancelInstance: (id: number, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 创建流程实例
     * @param {string} name
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createInstance: (name: string, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 通过 ID 获取流程实例
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getInstance: (id: number, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 获取实例变量
     * @param {number} id
     * @param {number} scope
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getInstanceVariables: (id: number, scope: number, cid?: string, options?: any) => Promise<RequestArgs>;
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
    getInstances: (name?: string, version?: number, status?: Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>, page?: number, size?: number, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 通过 ID 重试实例
     * @param {number} id
     * @param {number} scope
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    resolve: (id: number, scope: number, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 设置实例变量
     * @param {number} id
     * @param {number} scope
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    setInstanceVariables: (id: number, scope: number, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any) => Promise<RequestArgs>;
};
/**
 * InstancesApi - functional programming interface
 * @export
 */
export declare const InstancesApiFp: (configuration?: Configuration) => {
    /**
     *
     * @summary 通过 ID 取消运行实例
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    cancelInstance(id: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>>;
    /**
     *
     * @summary 创建流程实例
     * @param {string} name
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createInstance(name: string, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<InstanceObject>>;
    /**
     *
     * @summary 通过 ID 获取流程实例
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getInstance(id: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<InstanceObject>>;
    /**
     *
     * @summary 获取实例变量
     * @param {number} id
     * @param {number} scope
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getInstanceVariables(id: number, scope: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<{
        [key: string]: object;
    }>>;
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
    getInstances(name?: string, version?: number, status?: Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>, page?: number, size?: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<QueryResultInstanceObject>>;
    /**
     *
     * @summary 通过 ID 重试实例
     * @param {number} id
     * @param {number} scope
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    resolve(id: number, scope: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>>;
    /**
     *
     * @summary 设置实例变量
     * @param {number} id
     * @param {number} scope
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    setInstanceVariables(id: number, scope: number, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>>;
};
/**
 * InstancesApi - factory interface
 * @export
 */
export declare const InstancesApiFactory: (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) => {
    /**
     *
     * @summary 通过 ID 取消运行实例
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    cancelInstance(id: number, cid?: string, options?: any): AxiosPromise<void>;
    /**
     *
     * @summary 创建流程实例
     * @param {string} name
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createInstance(name: string, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): AxiosPromise<InstanceObject>;
    /**
     *
     * @summary 通过 ID 获取流程实例
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getInstance(id: number, cid?: string, options?: any): AxiosPromise<InstanceObject>;
    /**
     *
     * @summary 获取实例变量
     * @param {number} id
     * @param {number} scope
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getInstanceVariables(id: number, scope: number, cid?: string, options?: any): AxiosPromise<{
        [key: string]: object;
    }>;
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
    getInstances(name?: string, version?: number, status?: Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>, page?: number, size?: number, cid?: string, options?: any): AxiosPromise<QueryResultInstanceObject>;
    /**
     *
     * @summary 通过 ID 重试实例
     * @param {number} id
     * @param {number} scope
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    resolve(id: number, scope: number, cid?: string, options?: any): AxiosPromise<void>;
    /**
     *
     * @summary 设置实例变量
     * @param {number} id
     * @param {number} scope
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    setInstanceVariables(id: number, scope: number, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): AxiosPromise<void>;
};
/**
 * InstancesApi - object-oriented interface
 * @export
 * @class InstancesApi
 * @extends {BaseAPI}
 */
export declare class InstancesApi extends BaseAPI {
    /**
     *
     * @summary 通过 ID 取消运行实例
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof InstancesApi
     */
    cancelInstance(id: number, cid?: string, options?: any): Promise<import("axios").AxiosResponse<void>>;
    /**
     *
     * @summary 创建流程实例
     * @param {string} name
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof InstancesApi
     */
    createInstance(name: string, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): Promise<import("axios").AxiosResponse<InstanceObject>>;
    /**
     *
     * @summary 通过 ID 获取流程实例
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof InstancesApi
     */
    getInstance(id: number, cid?: string, options?: any): Promise<import("axios").AxiosResponse<InstanceObject>>;
    /**
     *
     * @summary 获取实例变量
     * @param {number} id
     * @param {number} scope
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof InstancesApi
     */
    getInstanceVariables(id: number, scope: number, cid?: string, options?: any): Promise<import("axios").AxiosResponse<{
        [key: string]: object;
    }>>;
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
     * @memberof InstancesApi
     */
    getInstances(name?: string, version?: number, status?: Set<'ACTIVE' | 'CANCELED' | 'COMPLETED' | 'INCIDENT' | 'RESOLVED'>, page?: number, size?: number, cid?: string, options?: any): Promise<import("axios").AxiosResponse<QueryResultInstanceObject>>;
    /**
     *
     * @summary 通过 ID 重试实例
     * @param {number} id
     * @param {number} scope
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof InstancesApi
     */
    resolve(id: number, scope: number, cid?: string, options?: any): Promise<import("axios").AxiosResponse<void>>;
    /**
     *
     * @summary 设置实例变量
     * @param {number} id
     * @param {number} scope
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof InstancesApi
     */
    setInstanceVariables(id: number, scope: number, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): Promise<import("axios").AxiosResponse<void>>;
}
/**
 * MessagesApi - axios parameter creator
 * @export
 */
export declare const MessagesApiAxiosParamCreator: (configuration?: Configuration) => {
    /**
     *
     * @summary 发布消息
     * @param {string} name
     * @param {string} key
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createMessage: (name: string, key: string, cid?: string, options?: any) => Promise<RequestArgs>;
};
/**
 * MessagesApi - functional programming interface
 * @export
 */
export declare const MessagesApiFp: (configuration?: Configuration) => {
    /**
     *
     * @summary 发布消息
     * @param {string} name
     * @param {string} key
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createMessage(name: string, key: string, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>>;
};
/**
 * MessagesApi - factory interface
 * @export
 */
export declare const MessagesApiFactory: (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) => {
    /**
     *
     * @summary 发布消息
     * @param {string} name
     * @param {string} key
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createMessage(name: string, key: string, cid?: string, options?: any): AxiosPromise<void>;
};
/**
 * MessagesApi - object-oriented interface
 * @export
 * @class MessagesApi
 * @extends {BaseAPI}
 */
export declare class MessagesApi extends BaseAPI {
    /**
     *
     * @summary 发布消息
     * @param {string} name
     * @param {string} key
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof MessagesApi
     */
    createMessage(name: string, key: string, cid?: string, options?: any): Promise<import("axios").AxiosResponse<void>>;
}
/**
 * ProcessesApi - axios parameter creator
 * @export
 */
export declare const ProcessesApiAxiosParamCreator: (configuration?: Configuration) => {
    /**
     *
     * @summary 创建流程
     * @param {string} body
     * @param {string} [cid]
     * @param {boolean} [base64]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createProcess: (body: string, cid?: string, base64?: boolean, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 通过名称获取流程
     * @param {string} name
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getProcess: (name: string, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 通过名称与版本号获取流程
     * @param {string} name
     * @param {number} version
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getProcess1: (name: string, version: number, cid?: string, options?: any) => Promise<RequestArgs>;
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
    getProcesses: (q?: string, cid?: string, page?: number, size?: number, options?: any) => Promise<RequestArgs>;
};
/**
 * ProcessesApi - functional programming interface
 * @export
 */
export declare const ProcessesApiFp: (configuration?: Configuration) => {
    /**
     *
     * @summary 创建流程
     * @param {string} body
     * @param {string} [cid]
     * @param {boolean} [base64]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createProcess(body: string, cid?: string, base64?: boolean, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>>;
    /**
     *
     * @summary 通过名称获取流程
     * @param {string} name
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getProcess(name: string, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ProcessObject>>;
    /**
     *
     * @summary 通过名称与版本号获取流程
     * @param {string} name
     * @param {number} version
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getProcess1(name: string, version: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<ProcessObject>>;
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
    getProcesses(q?: string, cid?: string, page?: number, size?: number, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<QueryResultProcessObject>>;
};
/**
 * ProcessesApi - factory interface
 * @export
 */
export declare const ProcessesApiFactory: (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) => {
    /**
     *
     * @summary 创建流程
     * @param {string} body
     * @param {string} [cid]
     * @param {boolean} [base64]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    createProcess(body: string, cid?: string, base64?: boolean, options?: any): AxiosPromise<void>;
    /**
     *
     * @summary 通过名称获取流程
     * @param {string} name
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getProcess(name: string, cid?: string, options?: any): AxiosPromise<ProcessObject>;
    /**
     *
     * @summary 通过名称与版本号获取流程
     * @param {string} name
     * @param {number} version
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getProcess1(name: string, version: number, cid?: string, options?: any): AxiosPromise<ProcessObject>;
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
    getProcesses(q?: string, cid?: string, page?: number, size?: number, options?: any): AxiosPromise<QueryResultProcessObject>;
};
/**
 * ProcessesApi - object-oriented interface
 * @export
 * @class ProcessesApi
 * @extends {BaseAPI}
 */
export declare class ProcessesApi extends BaseAPI {
    /**
     *
     * @summary 创建流程
     * @param {string} body
     * @param {string} [cid]
     * @param {boolean} [base64]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ProcessesApi
     */
    createProcess(body: string, cid?: string, base64?: boolean, options?: any): Promise<import("axios").AxiosResponse<void>>;
    /**
     *
     * @summary 通过名称获取流程
     * @param {string} name
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ProcessesApi
     */
    getProcess(name: string, cid?: string, options?: any): Promise<import("axios").AxiosResponse<ProcessObject>>;
    /**
     *
     * @summary 通过名称与版本号获取流程
     * @param {string} name
     * @param {number} version
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ProcessesApi
     */
    getProcess1(name: string, version: number, cid?: string, options?: any): Promise<import("axios").AxiosResponse<ProcessObject>>;
    /**
     *
     * @summary 获取流程列表
     * @param {string} [q]
     * @param {string} [cid]
     * @param {number} [page]
     * @param {number} [size]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof ProcessesApi
     */
    getProcesses(q?: string, cid?: string, page?: number, size?: number, options?: any): Promise<import("axios").AxiosResponse<QueryResultProcessObject>>;
}
/**
 * TriggersApi - axios parameter creator
 * @export
 */
export declare const TriggersApiAxiosParamCreator: (configuration?: Configuration) => {
    /**
     *
     * @summary 获取触发器支持的事件
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getOperations: (options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 获取流程关联的触发器
     * @param {string} process
     * @param {string} [opt]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getTriggerKeys: (process: string, opt?: string, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 获取触发器的目标流程
     * @param {string} key
     * @param {string} [opt]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getTriggerProcess: (key: string, opt?: string, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 设置触发器
     * @param {string} key
     * @param {string} opt
     * @param {Set<string>} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    setTriggerProcess: (key: string, opt: string, requestBody: Set<string>, cid?: string, options?: any) => Promise<RequestArgs>;
};
/**
 * TriggersApi - functional programming interface
 * @export
 */
export declare const TriggersApiFp: (configuration?: Configuration) => {
    /**
     *
     * @summary 获取触发器支持的事件
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getOperations(options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<Array<string>>>;
    /**
     *
     * @summary 获取流程关联的触发器
     * @param {string} process
     * @param {string} [opt]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getTriggerKeys(process: string, opt?: string, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<Array<string>>>;
    /**
     *
     * @summary 获取触发器的目标流程
     * @param {string} key
     * @param {string} [opt]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getTriggerProcess(key: string, opt?: string, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<Array<string>>>;
    /**
     *
     * @summary 设置触发器
     * @param {string} key
     * @param {string} opt
     * @param {Set<string>} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    setTriggerProcess(key: string, opt: string, requestBody: Set<string>, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>>;
};
/**
 * TriggersApi - factory interface
 * @export
 */
export declare const TriggersApiFactory: (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) => {
    /**
     *
     * @summary 获取触发器支持的事件
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getOperations(options?: any): AxiosPromise<Array<string>>;
    /**
     *
     * @summary 获取流程关联的触发器
     * @param {string} process
     * @param {string} [opt]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getTriggerKeys(process: string, opt?: string, cid?: string, options?: any): AxiosPromise<Array<string>>;
    /**
     *
     * @summary 获取触发器的目标流程
     * @param {string} key
     * @param {string} [opt]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getTriggerProcess(key: string, opt?: string, cid?: string, options?: any): AxiosPromise<Array<string>>;
    /**
     *
     * @summary 设置触发器
     * @param {string} key
     * @param {string} opt
     * @param {Set<string>} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    setTriggerProcess(key: string, opt: string, requestBody: Set<string>, cid?: string, options?: any): AxiosPromise<void>;
};
/**
 * TriggersApi - object-oriented interface
 * @export
 * @class TriggersApi
 * @extends {BaseAPI}
 */
export declare class TriggersApi extends BaseAPI {
    /**
     *
     * @summary 获取触发器支持的事件
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof TriggersApi
     */
    getOperations(options?: any): Promise<import("axios").AxiosResponse<string[]>>;
    /**
     *
     * @summary 获取流程关联的触发器
     * @param {string} process
     * @param {string} [opt]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof TriggersApi
     */
    getTriggerKeys(process: string, opt?: string, cid?: string, options?: any): Promise<import("axios").AxiosResponse<string[]>>;
    /**
     *
     * @summary 获取触发器的目标流程
     * @param {string} key
     * @param {string} [opt]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof TriggersApi
     */
    getTriggerProcess(key: string, opt?: string, cid?: string, options?: any): Promise<import("axios").AxiosResponse<string[]>>;
    /**
     *
     * @summary 设置触发器
     * @param {string} key
     * @param {string} opt
     * @param {Set<string>} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof TriggersApi
     */
    setTriggerProcess(key: string, opt: string, requestBody: Set<string>, cid?: string, options?: any): Promise<import("axios").AxiosResponse<void>>;
}
/**
 * UserTasksApi - axios parameter creator
 * @export
 */
export declare const UserTasksApiAxiosParamCreator: (configuration?: Configuration) => {
    /**
     *
     * @summary 完成用户任务
     * @param {number} id
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    completeUserTask: (id: number, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 获取用户任务
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getUserTask: (id: number, cid?: string, options?: any) => Promise<RequestArgs>;
    /**
     *
     * @summary 获取用户任务
     * @param {string} [name]
     * @param {number} [version]
     * @param {'DONE' | 'ACTIVE'} [status]
     * @param {number} [page]
     * @param {number} [size]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getUserTasks: (name?: string, version?: number, status?: 'DONE' | 'ACTIVE', page?: number, size?: number, cid?: string, options?: any) => Promise<RequestArgs>;
};
/**
 * UserTasksApi - functional programming interface
 * @export
 */
export declare const UserTasksApiFp: (configuration?: Configuration) => {
    /**
     *
     * @summary 完成用户任务
     * @param {number} id
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    completeUserTask(id: number, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<void>>;
    /**
     *
     * @summary 获取用户任务
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getUserTask(id: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<UserTask>>;
    /**
     *
     * @summary 获取用户任务
     * @param {string} [name]
     * @param {number} [version]
     * @param {'DONE' | 'ACTIVE'} [status]
     * @param {number} [page]
     * @param {number} [size]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getUserTasks(name?: string, version?: number, status?: 'DONE' | 'ACTIVE', page?: number, size?: number, cid?: string, options?: any): Promise<(axios?: AxiosInstance, basePath?: string) => AxiosPromise<QueryResultUserTask>>;
};
/**
 * UserTasksApi - factory interface
 * @export
 */
export declare const UserTasksApiFactory: (configuration?: Configuration, basePath?: string, axios?: AxiosInstance) => {
    /**
     *
     * @summary 完成用户任务
     * @param {number} id
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    completeUserTask(id: number, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): AxiosPromise<void>;
    /**
     *
     * @summary 获取用户任务
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getUserTask(id: number, cid?: string, options?: any): AxiosPromise<UserTask>;
    /**
     *
     * @summary 获取用户任务
     * @param {string} [name]
     * @param {number} [version]
     * @param {'DONE' | 'ACTIVE'} [status]
     * @param {number} [page]
     * @param {number} [size]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     */
    getUserTasks(name?: string, version?: number, status?: 'DONE' | 'ACTIVE', page?: number, size?: number, cid?: string, options?: any): AxiosPromise<QueryResultUserTask>;
};
/**
 * UserTasksApi - object-oriented interface
 * @export
 * @class UserTasksApi
 * @extends {BaseAPI}
 */
export declare class UserTasksApi extends BaseAPI {
    /**
     *
     * @summary 完成用户任务
     * @param {number} id
     * @param {{ [key: string]: object; }} requestBody
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof UserTasksApi
     */
    completeUserTask(id: number, requestBody: {
        [key: string]: object;
    }, cid?: string, options?: any): Promise<import("axios").AxiosResponse<void>>;
    /**
     *
     * @summary 获取用户任务
     * @param {number} id
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof UserTasksApi
     */
    getUserTask(id: number, cid?: string, options?: any): Promise<import("axios").AxiosResponse<UserTask>>;
    /**
     *
     * @summary 获取用户任务
     * @param {string} [name]
     * @param {number} [version]
     * @param {'DONE' | 'ACTIVE'} [status]
     * @param {number} [page]
     * @param {number} [size]
     * @param {string} [cid]
     * @param {*} [options] Override http request option.
     * @throws {RequiredError}
     * @memberof UserTasksApi
     */
    getUserTasks(name?: string, version?: number, status?: 'DONE' | 'ACTIVE', page?: number, size?: number, cid?: string, options?: any): Promise<import("axios").AxiosResponse<QueryResultUserTask>>;
}
