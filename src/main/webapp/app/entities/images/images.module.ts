import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ImagesComponent } from './list/images.component';
import { ImagesDetailComponent } from './detail/images-detail.component';
import { ImagesUpdateComponent } from './update/images-update.component';
import { ImagesDeleteDialogComponent } from './delete/images-delete-dialog.component';
import { ImagesRoutingModule } from './route/images-routing.module';

@NgModule({
  imports: [SharedModule, ImagesRoutingModule],
  declarations: [ImagesComponent, ImagesDetailComponent, ImagesUpdateComponent, ImagesDeleteDialogComponent],
})
export class ImagesModule {}
