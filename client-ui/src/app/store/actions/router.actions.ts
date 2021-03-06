/*
 * Copyright 2019 ABSA Group Limited
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import {Action} from "@ngrx/store";
import {Params} from "@angular/router";

export enum RouterActionTypes {
    GO = "[Router] Go",
    MERGE_PARAMS = "[Router] Merge Params"
}

export class Go implements Action {
    public readonly type = RouterActionTypes.GO
    constructor(public payload: Params) { }
}

export class MergeParams implements Action {
    public readonly type = RouterActionTypes.MERGE_PARAMS
    constructor(public payload: Params) { }
}

export type RouterActions
    = Go
    | MergeParams
