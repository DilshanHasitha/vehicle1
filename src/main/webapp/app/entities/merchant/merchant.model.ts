import { IImages } from 'app/entities/images/images.model';
import { IExUser } from 'app/entities/ex-user/ex-user.model';

export interface IMerchant {
  id: number;
  code?: string | null;
  merchantSecret?: string | null;
  name?: string | null;
  creditLimit?: number | null;
  isActive?: boolean | null;
  phone?: string | null;
  addressLine1?: string | null;
  addressLine2?: string | null;
  city?: string | null;
  country?: string | null;
  percentage?: number | null;
  creditScore?: number | null;
  email?: string | null;
  rating?: number | null;
  leadTime?: number | null;
  isSandBox?: boolean | null;
  storeDescription?: string | null;
  storeSecondaryDescription?: string | null;
  images?: Pick<IImages, 'id'> | null;
  exUsers?: Pick<IExUser, 'id'>[] | null;
}

export type NewMerchant = Omit<IMerchant, 'id'> & { id: null };
