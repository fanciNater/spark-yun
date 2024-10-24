export interface UpdateEngineNodeReq {
  clusterId: string | undefined

  id: string
  name: string

  host: string

  comment: string

  post: string

  username: string

  password: string

  agentHomePath: string

  agentPort: string

  hadoopHomePath: string
}
