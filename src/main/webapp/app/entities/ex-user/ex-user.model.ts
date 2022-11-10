import { IUser } from 'app/entities/user/user.model';
import { IMerchant } from 'app/entities/merchant/merchant.model';

export interface IExUser {
  id: number;
  login?: string | null;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  isActive?: boolean | null;
  phone?: string | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  city?: string | null;
  country?: string | null;
  image?: string | null;
  imageContentType?: string | null;
  userLimit?: number | null;
  creditScore?: number | null;
  relatedUser?: Pick<IUser, 'id'> | null;
  merchants?: Pick<IMerchant, 'id'>[] | null;
}

export type NewExUser = Omit<IExUser, 'id'> & { id: null };
