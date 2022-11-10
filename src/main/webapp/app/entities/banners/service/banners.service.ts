import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBanners, NewBanners } from '../banners.model';

export type PartialUpdateBanners = Partial<IBanners> & Pick<IBanners, 'id'>;

export type EntityResponseType = HttpResponse<IBanners>;
export type EntityArrayResponseType = HttpResponse<IBanners[]>;

@Injectable({ providedIn: 'root' })
export class BannersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/banners');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(banners: NewBanners): Observable<EntityResponseType> {
    return this.http.post<IBanners>(this.resourceUrl, banners, { observe: 'response' });
  }

  update(banners: IBanners): Observable<EntityResponseType> {
    return this.http.put<IBanners>(`${this.resourceUrl}/${this.getBannersIdentifier(banners)}`, banners, { observe: 'response' });
  }

  partialUpdate(banners: PartialUpdateBanners): Observable<EntityResponseType> {
    return this.http.patch<IBanners>(`${this.resourceUrl}/${this.getBannersIdentifier(banners)}`, banners, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBanners>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBanners[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBannersIdentifier(banners: Pick<IBanners, 'id'>): number {
    return banners.id;
  }

  compareBanners(o1: Pick<IBanners, 'id'> | null, o2: Pick<IBanners, 'id'> | null): boolean {
    return o1 && o2 ? this.getBannersIdentifier(o1) === this.getBannersIdentifier(o2) : o1 === o2;
  }

  addBannersToCollectionIfMissing<Type extends Pick<IBanners, 'id'>>(
    bannersCollection: Type[],
    ...bannersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const banners: Type[] = bannersToCheck.filter(isPresent);
    if (banners.length > 0) {
      const bannersCollectionIdentifiers = bannersCollection.map(bannersItem => this.getBannersIdentifier(bannersItem)!);
      const bannersToAdd = banners.filter(bannersItem => {
        const bannersIdentifier = this.getBannersIdentifier(bannersItem);
        if (bannersCollectionIdentifiers.includes(bannersIdentifier)) {
          return false;
        }
        bannersCollectionIdentifiers.push(bannersIdentifier);
        return true;
      });
      return [...bannersToAdd, ...bannersCollection];
    }
    return bannersCollection;
  }
}
