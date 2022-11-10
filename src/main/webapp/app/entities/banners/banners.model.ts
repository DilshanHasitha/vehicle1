import { IImages } from 'app/entities/images/images.model';

export interface IBanners {
  id: number;
  code?: string | null;
  heading?: string | null;
  description?: string | null;
  link?: string | null;
  images?: Pick<IImages, 'id'>[] | null;
}

export type NewBanners = Omit<IBanners, 'id'> & { id: null };
