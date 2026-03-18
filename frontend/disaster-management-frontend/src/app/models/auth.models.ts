export interface LoginRequest {
  email: string;
  password: string;
}

export interface SignupRequest {
  name: string;
  email: string;
  password: string;
  role?: string;
  mobile?: string;
  region?: string;
}

export interface JwtResponse {
  token: string;
  type: string;
  id: number;
  email: string;
  name: string;
  roles: string[];
}

export interface MessageResponse {
  message: string;
}
