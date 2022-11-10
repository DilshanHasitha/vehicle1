import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBanners } from '../banners.model';
import { BannersService } from '../service/banners.service';

@Injectable({ providedIn: 'root' })
export class BannersRoutingResolveService implements Resolve<IBanners | null> {
  constructor(protected service: BannersService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBanners | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((banners: HttpResponse<IBanners>) => {
          if (banners.body) {
            return of(banners.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
