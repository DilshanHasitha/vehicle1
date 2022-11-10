import { IEmployee } from 'app/entities/employee/employee.model';
import { IBanners } from 'app/entities/banners/banners.model';

export interface IImages {
  id: number;
  imglobContentType?: string | null;
  imageURL?: string | null;
  imageName?: string | null;
  lowResURL?: string | null;
  originalURL?: string | null;
  imageBlob?: string | null;
  imageBlobContentType?: string | null;
  employee?: Pick<IEmployee, 'id'> | null;
  banners?: Pick<IBanners, 'id'>[] | null;
}

export type NewImages = Omit<IImages, 'id'> & { id: null };
